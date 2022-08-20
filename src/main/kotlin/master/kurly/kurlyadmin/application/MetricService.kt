package master.kurly.kurlyadmin.application

import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.metric.MetricHistory
import master.kurly.kurlyadmin.domain.metric.MetricRepository
import master.kurly.kurlyadmin.domain.product.Product
import master.kurly.kurlyadmin.domain.subscriber.Subscriber
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MetricService(
    private val metricRepository: MetricRepository
) {

    fun getAllMetrics(): List<Metric> {
        return this.metricRepository.getAllMetrics()
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

    fun getProductsOfMetric(id: Long): List<Product>? {
        return this.getMetricById(id)?.let { metric ->
            this.metricRepository.getProductsOfMetric(metric)
        }
    }

    fun createMetric(metric: Metric): Boolean {
        return this.metricRepository.createMetric(metric)
    }

    fun deleteMetricById(metricId: Long): Boolean {
        return this.metricRepository.deleteMetric(metricId)
    }

    fun addSubscriberToMetric(metricId: Long, subscriberId: Long): Boolean {
        return this.metricRepository.addSubscriberToMetric(metricId, subscriberId)
    }

    fun removeSubscriberToMetric(metricId: Long, subscriberId: Long): Boolean {
        return this.metricRepository.removeSubscriberToMetric(metricId, subscriberId)
    }
}