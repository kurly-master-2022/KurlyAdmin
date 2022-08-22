package master.kurly.kurlyadmin.infrastructure.implementation

import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.metric.MetricHistory
import master.kurly.kurlyadmin.domain.metric.MetricRepository
import master.kurly.kurlyadmin.domain.product.Product
import master.kurly.kurlyadmin.domain.product.ProductMetricImportance
import master.kurly.kurlyadmin.domain.subscriber.Subscriber
import master.kurly.kurlyadmin.infrastructure.api.CloudWatchApi
import master.kurly.kurlyadmin.infrastructure.entity.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Repository
class MetricRepositoryImpl(
    private val metricEntityRepository: MetricEntityRepository,
    private val subscriberEntityRepository: SubscriberEntityRepository,
    private val productMetricEntityRepository: ProductMetricEntityRepository,
    private val metricSubscriberEntityRepository: MetricSubscriberEntityRepository
): MetricRepository {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun getAllMetrics(): List<Metric> {
        return this.metricEntityRepository.findAll().map { it.toMetric() }
    }

    override fun getAvailableMetrics(): List<Metric> {
        return this.metricEntityRepository.findAllByIsAvailable(true).map { it.toMetric() }
    }

    override fun getMetricById(id: Long): Metric? {
        return this.metricEntityRepository.findById(id).orElse(null)?.toMetric()
    }

    override fun createMetric(metric: Metric): Boolean {
        this.metricEntityRepository.save(
            MetricEntity.fromMetric(metric).apply { this.id = null }
        )
        return true
    }

    override fun deleteMetric(id: Long): Boolean {
        this.metricEntityRepository.findById(id).orElse(null)
            ?.also { metricEntity ->
                // delete mapping of product
                this.productMetricEntityRepository.findByMetricId(id)
                    .let { this.productMetricEntityRepository.deleteAll(it) }

                // delete mapping of subscriber
                this.metricSubscriberEntityRepository.findByMetricId(id)
                    .let { this.metricSubscriberEntityRepository.deleteAll(it) }

                // delete Metric from DB
                this.metricEntityRepository.delete(metricEntity)
            }
            ?: run { this.logger.error("메트릭 id 에 해당하는 메트릭이 존재하지 않습니다!") }
        return true
    }

    override fun getProductsOfMetric(metric: Metric): Map<Product, ProductMetricImportance> {
        return this.metricEntityRepository.findById(metric.id).orElse(null)
            ?.let { metricEntity ->
                this.productMetricEntityRepository.findByMetricId(metricEntity.id!!)
                    .associate { it.productEntity!!.toProduct() to it.preference }
            }
            ?: mapOf()
    }

    override fun getSubscribersOfMetric(metric: Metric): List<Subscriber> {
        return this.metricEntityRepository.findById(metric.id).orElse(null)
            ?.let { metricEntity ->
                this.metricSubscriberEntityRepository.findByMetricId(metricEntity.id!!)
                    .map { it.subscriberEntity!!.toSubscriber() }
            }
            ?: listOf()
    }

    @Transactional
    override fun addSubscriberToMetric(metricId: Long, subscriberIds: List<Long>): Boolean {
        val metric = this.metricEntityRepository.findById(metricId).orElse(null)
        val subscribers = this.subscriberEntityRepository.findAllById(subscriberIds)

        if (metric == null) {
            this.logger.warn("주어진 metric id 에 해당하는 Metric 을 찾지 못했습니다. 작업을 실행하지 않습니다.")
            return false
        }

        if (subscribers.count() != subscriberIds.size) {
            this.logger.warn("주어진 구독자 id 들이 유효하지 않습니다. 작업을 실행하지 않습니다.")
            return false
        }

        subscribers.forEach { subscriberEntity ->
            if (this.metricSubscriberEntityRepository.findByMetricEntityAndSubscriberEntity(
                    metric,
                    subscriberEntity
                ) == null
            ) {
                MetricSubscriberEntity(null, metric, subscriberEntity)
                    .let { this.metricSubscriberEntityRepository.save(it) }
            }
        }

        return true
    }

    @Transactional
    override fun removeSubscriberToMetric(metricId: Long, subscriberIds: List<Long>): Boolean {
        val metric = this.metricEntityRepository.findById(metricId).orElse(null)
        val subscribers = this.subscriberEntityRepository.findAllById(subscriberIds)

        if (metric == null) {
            this.logger.warn("주어진 metric id 에 해당하는 Metric 을 찾지 못했습니다. 작업을 실행하지 않습니다.")
            return false
        }

        if (subscribers.count() != subscriberIds.size) {
            this.logger.warn("주어진 구독자 id 들이 유효하지 않습니다. 작업을 실행하지 않습니다.")
            return false
        }

        subscribers
            .mapNotNull { this.metricSubscriberEntityRepository.findByMetricEntityAndSubscriberEntity(metric, it) }
            .let { this.metricSubscriberEntityRepository.deleteAll(it) }

        return true
    }

}