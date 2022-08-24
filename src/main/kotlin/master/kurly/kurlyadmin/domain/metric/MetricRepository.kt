package master.kurly.kurlyadmin.domain.metric

import master.kurly.kurlyadmin.domain.product.Product
import master.kurly.kurlyadmin.domain.product.ProductMetricImportance
import master.kurly.kurlyadmin.domain.subscriber.Subscriber
import java.time.LocalDateTime

interface MetricRepository {
    fun getAllMetrics(): List<Metric>
    fun getAvailableMetrics(): List<Metric>
    fun getMetricById(id: Long): Metric?
    fun modifyMetric(metric: Metric): Boolean
    fun createMetric(metric: Metric): Boolean
    fun deleteMetric(id: Long): Boolean

    fun getProductsOfMetric(metric: Metric): Map<Product, ProductMetricImportance>

    fun getSubscribersOfMetric(metric: Metric): List<Subscriber>
    fun addSubscriberToMetric(metricId: Long, subscriberIds: List<Long>): Boolean
    fun removeSubscriberToMetric(metricId: Long, subscriberIds: List<Long>): Boolean

}

interface MetricValueRepository {
    fun isMetricAlarmTriggered(metric: Metric): Boolean
    fun getMetricValueHistory(metric: Metric, startAt: LocalDateTime, endAt: LocalDateTime): MetricHistory?
}

interface MetricWorkflowManager {

    // 메트릭 새로 등록시 개발자에게 일감 알려주는 기능
    fun registerMetricWorkflowJob(metric: Metric): Boolean

    // 메트릭 새로 등록시 워크플로 등록 요청을 보내는 기능
    fun createMetricWorkflow(metric: Metric): Boolean

    // 개발자가 메트릭 개발 완료 후 호출해 메트릭 워크플로우 활성화하는 기능
    fun activateMetricWorkflow(metric: Metric): Boolean

    // 메트릭 워크플로우를 삭제하는 기능
    fun deleteMetricWorkflow(metric: Metric): Boolean
}