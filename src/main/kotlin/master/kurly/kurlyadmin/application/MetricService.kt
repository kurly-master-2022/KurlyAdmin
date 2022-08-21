package master.kurly.kurlyadmin.application

import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.metric.MetricHistory
import master.kurly.kurlyadmin.domain.metric.MetricRepository
import master.kurly.kurlyadmin.domain.product.Product
import master.kurly.kurlyadmin.domain.product.ProductMetricImportance
import master.kurly.kurlyadmin.domain.subscriber.Subscriber
import master.kurly.kurlyadmin.infrastructure.controller.MetricCreateDto
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MetricService(
    private val metricRepository: MetricRepository
) {

    fun getAllMetrics(): List<Metric> {
        return this.metricRepository.getAllMetrics()
    }

    fun getAvailableMetrics(): List<Metric>{
        return this.metricRepository.getAvailableMetrics()
    }

    fun getMetricById(id: Long): Metric? {
        return this.metricRepository.getMetricById(id)
    }

    fun getMetricHistory(id: Long, startString: String, endString: String): MetricHistory? {
        val startDatetime = LocalDateTime.parse(startString)
        val endDatetime = LocalDateTime.parse(endString)

        return this.getMetricById(id)?.let { metric ->
            this.metricRepository.getMetricValueHistory(metric, startDatetime, endDatetime)
        }
    }

    fun getSubscribersOfMetric(id: Long): List<Subscriber>? {
        return this.getMetricById(id)?.let { metric ->
            this.metricRepository.getSubscribersOfMetric(metric)
        }
    }

    fun getProductsOfMetric(id: Long): Map<Product, ProductMetricImportance>? {
        return this.getMetricById(id)?.let { metric ->
            this.metricRepository.getProductsOfMetric(metric)
        }
    }

    fun createMetric(metricCreateDto: MetricCreateDto): Boolean {
        return Metric(
            id = 0,
            nickname = metricCreateDto.nickname,
            name = metricCreateDto.name,
            sourceType = metricCreateDto.sourceType,
            source = metricCreateDto.source,
            isScheduled = metricCreateDto.scheduled,
            cronSchedule = metricCreateDto.schedCron,
            s3ObjectKey = null,
            threshold = metricCreateDto.alarmThreshold,
            thresholdDirection = metricCreateDto.alarmComparator,
            description = metricCreateDto.description,
            isAvailable = false
        ).let {
            this.metricRepository.createMetric(it)
        }
    }

    fun deleteMetricById(metricId: Long): Boolean {
        return this.metricRepository.deleteMetric(metricId)
    }

    fun addSubscriberToMetric(metricId: Long, subscriberIds: List<Long>): Boolean {
        return this.metricRepository.addSubscriberToMetric(metricId, subscriberIds)
    }

    fun removeSubscriberToMetric(metricId: Long, subscriberIds: List<Long>): Boolean {
        return this.metricRepository.removeSubscriberToMetric(metricId, subscriberIds)
    }
}