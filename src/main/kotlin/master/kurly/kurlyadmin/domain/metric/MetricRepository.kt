package master.kurly.kurlyadmin.domain.metric

import master.kurly.kurlyadmin.domain.product.Product
import master.kurly.kurlyadmin.domain.subscriber.Subscriber
import java.time.LocalDateTime

interface MetricRepository {
    fun getAllMetrics(): List<Metric>
    fun getMetricById(id: Long): Metric?
    fun createMetric(metric: Metric): Boolean
    fun deleteMetric(id: Long): Boolean

    fun getProductsOfMetric(metric: Metric): List<Product>

    fun getSubscribersOfMetric(metric: Metric): List<Subscriber>
    fun addSubscriberToMetric(metricId: Long, subscriberId: Long): Boolean
    fun removeSubscriberToMetric(metricId: Long, subscriberId: Long): Boolean

    fun isMetricAlarmTriggered(metric: Metric): Boolean
    fun getMetricValueHistory(metric: Metric, startAt: LocalDateTime, endAt: LocalDateTime): MetricHistory?
}