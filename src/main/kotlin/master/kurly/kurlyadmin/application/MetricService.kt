package master.kurly.kurlyadmin.application

import master.kurly.kurlyadmin.domain.metric.*
import master.kurly.kurlyadmin.domain.product.Product
import master.kurly.kurlyadmin.domain.product.ProductMetricImportance
import master.kurly.kurlyadmin.domain.subscriber.Subscriber
import master.kurly.kurlyadmin.infrastructure.controller.MetricCreateDto
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class MetricService(
    private val metricRepository: MetricRepository,
    private val metricValueRepository: MetricValueRepository,
    private val metricWorkflowManager: MetricWorkflowManager,
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
            this.metricValueRepository.getMetricValueHistory(metric, startDatetime, endDatetime)
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

    @Transactional
    fun createMetric(metricCreateDto: MetricCreateDto) {
        val metric = Metric(
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
            isAvailable = !metricCreateDto.scheduled
        )
        this.metricRepository.createMetric(metric)
        this.metricWorkflowManager.createMetricWorkflow(metric)
            .let { if(!it) throw RuntimeException("메트릭 워크플로우 생성 실패!") }
        this.metricWorkflowManager.registerMetricWorkflowJob(metric)
            .let { if(!it) throw RuntimeException("메트릭 워크플로우 일감 생성 실패!") }

    }

    @Transactional
    fun deleteMetricById(metricId: Long) {
        return this.metricRepository.getMetricById(metricId)
            ?.let { metric ->
                this.metricRepository.deleteMetric(metricId)
                this.metricWorkflowManager.deleteMetricWorkflow(metric)
                    .let { if(!it) throw RuntimeException("메트릭 워크플로우 삭제 실패!") }
            }
            ?: run {throw RuntimeException("메트릭 조회 실패!") }
    }

    fun addSubscriberToMetric(metricId: Long, subscriberIds: List<Long>): Boolean {
        return this.metricRepository.addSubscriberToMetric(metricId, subscriberIds)
    }

    fun removeSubscriberToMetric(metricId: Long, subscriberIds: List<Long>): Boolean {
        return this.metricRepository.removeSubscriberToMetric(metricId, subscriberIds)
    }


    fun activateMetricWorkflow(metricId: Long): Boolean{
        return this.metricRepository.getMetricById(metricId)
            ?.let { metric -> this.metricWorkflowManager.activateMetricWorkflow(metric) }
            ?: false
    }
}