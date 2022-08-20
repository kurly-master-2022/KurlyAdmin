package master.kurly.kurlyadmin.infrastructure.api

import master.kurly.kurlyadmin.domain.metric.Metric as DomainMetric
import com.amazonaws.services.cloudwatch.AmazonCloudWatch
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder
import com.amazonaws.services.cloudwatch.model.GetMetricDataRequest
import com.amazonaws.services.cloudwatch.model.Metric
import com.amazonaws.services.cloudwatch.model.MetricDataQuery
import com.amazonaws.services.cloudwatch.model.MetricStat
import master.kurly.kurlyadmin.domain.metric.MetricHistory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Component
class CloudWatchApi(
    @Value("\${custom-variable.amazon.cloudwatch.namespace}") private val namespace: String
) {
    private val cloudWatch: AmazonCloudWatch = AmazonCloudWatchClientBuilder.defaultClient()
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val seoulZone = ZoneId.of("Asia/Seoul")

    fun getMetricData(
        metric: DomainMetric,
        startDatetime: ZonedDateTime,
        endDatetime: ZonedDateTime
    ): MetricHistory? {
        val metricQuery = MetricDataQuery()
            .withId("test1")
            .withMetricStat(
                MetricStat()
                    .withMetric(
                        Metric()
                            .withMetricName(metric.nickname)
                            .withNamespace(namespace)
                    )
                    // period 가 3시간보다 전일 경우, 정확도를 제한해야 함. 설명에 읳면 63일 이전의 경우 최소 1시간이기 때문에, 1시간으로 맞춰줌.
                    .withPeriod(3600)
                    .withStat("Average")
            )
            .withReturnData(true)

        return runCatching {
            this.cloudWatch.getMetricData(
                GetMetricDataRequest()
                    .withStartTime(Date.from(startDatetime.toInstant()))
                    .withEndTime(Date.from(endDatetime.toInstant()))
                    .withMetricDataQueries(metricQuery)
            )
        }.mapCatching { getMetricDataResult ->
            this.logger.info("query result : $getMetricDataResult")
            MetricHistory(
                metric,
                getMetricDataResult.metricDataResults[0].timestamps.map{ LocalDateTime.ofInstant(it.toInstant(), seoulZone) },
                getMetricDataResult.metricDataResults[0].values
            )
        }.onFailure { err ->
            this.logger.error("쿼리 실패! : $err")
            err.printStackTrace()
        }.getOrNull()
    }
}