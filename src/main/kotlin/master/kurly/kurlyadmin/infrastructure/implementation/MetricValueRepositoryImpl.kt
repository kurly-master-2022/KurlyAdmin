package master.kurly.kurlyadmin.infrastructure.implementation

import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.metric.MetricHistory
import master.kurly.kurlyadmin.domain.metric.MetricValueRepository
import master.kurly.kurlyadmin.infrastructure.api.CloudWatchApi
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Component
class MetricValueRepositoryImpl(
    private val cloudWatchApi: CloudWatchApi
): MetricValueRepository {
    private val seoulZone = ZoneId.of("Asia/Seoul")

    override fun isMetricAlarmTriggered(metric: Metric): Boolean {
        TODO()
    }

    override fun getMetricValueHistory(metric: Metric, startAt: LocalDateTime, endAt: LocalDateTime): MetricHistory? {
        return this.cloudWatchApi.getMetricData(
            metric,
            ZonedDateTime.of(startAt, seoulZone),
            ZonedDateTime.of(endAt, seoulZone)
        )
    }
}