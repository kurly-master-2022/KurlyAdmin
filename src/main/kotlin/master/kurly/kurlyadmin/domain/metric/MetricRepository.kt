package master.kurly.kurlyadmin.domain.metric

import master.kurly.kurlyadmin.domain.product.Product
import master.kurly.kurlyadmin.domain.subscriber.Subscriber

interface MetricRepository {
    fun getAllMetrics(): List<Metric>
    fun getProductsOfMetric(metric: Metric): List<Product>?
    fun getSubscribersOfMetric(metric: Metric): List<Subscriber>?
    fun isAlarmAvailable(metric: Metric): Boolean
}

interface MetricHistoryRepository {
    fun getMetricValueHistory(metric: Metric): MetricHistory?
}