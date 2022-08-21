package master.kurly.kurlyadmin.domain.metric

import master.kurly.kurlyadmin.domain.product.Product
import master.kurly.kurlyadmin.domain.product.ProductMetricImportance
import master.kurly.kurlyadmin.domain.subscriber.Subscriber
import java.time.LocalDateTime

interface MetricRepository {
    fun getAllMetrics(): List<Metric>
    fun getAvailableMetrics(): List<Metric>
    fun getMetricById(id: Long): Metric?
    fun createMetric(metric: Metric): Boolean
    fun deleteMetric(id: Long): Boolean

    fun getProductsOfMetric(metric: Metric): Map<Product, ProductMetricImportance>

    fun getSubscribersOfMetric(metric: Metric): List<Subscriber>
    fun addSubscriberToMetric(metricId: Long, subscriberIds: List<Long>): Boolean
    fun removeSubscriberToMetric(metricId: Long, subscriberIds: List<Long>): Boolean

    fun isMetricAlarmTriggered(metric: Metric): Boolean
    fun getMetricValueHistory(metric: Metric, startAt: LocalDateTime, endAt: LocalDateTime): MetricHistory?
}