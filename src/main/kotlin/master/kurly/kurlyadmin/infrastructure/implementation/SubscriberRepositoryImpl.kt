package master.kurly.kurlyadmin.infrastructure.implementation

import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.subscriber.Subscriber
import master.kurly.kurlyadmin.domain.subscriber.SubscriberRepository
import master.kurly.kurlyadmin.infrastructure.controller.MetricSubscriberDto
import master.kurly.kurlyadmin.infrastructure.entity.*
import org.springframework.stereotype.Repository

@Repository
class SubscriberRepositoryImpl(
    private val subscriberEntityRepository: SubscriberEntityRepository,
    private val metricSubscriberEntityRepository: MetricSubscriberEntityRepository,
    private val metricEntityRepository: MetricEntityRepository
): SubscriberRepository {
    override fun getAllSubscribers(): List<Subscriber> {
        return this.subscriberEntityRepository.findAll().map { it.toSubscriber() }
    }

    override fun getSubscriberById(id: Long): Subscriber? {
        return this.subscriberEntityRepository.findById(id).orElse(null)?.toSubscriber()
    }

    override fun createSubscriber(subscriber: Subscriber, metricIds: List<Long>): Boolean {
        SubscriberEntity.newSubscriber(subscriber)
            .let { subscriberEntity ->
                this.subscriberEntityRepository.save(subscriberEntity)
                this.metricSubscriberEntityRepository.saveAll(
                    this.metricEntityRepository.findAllById(metricIds).map {
                        MetricSubscriberEntity(null, it, subscriberEntity)
                    }
                )
            }
        return true
    }

    override fun deleteSubscriber(subscriberId: Long): Boolean {
        return this.subscriberEntityRepository.findById(subscriberId).orElse(null)
            ?.let { subscriberEntity ->
                this.metricSubscriberEntityRepository.findBySubscriberId(subscriberEntity.id!!)
                    .let { this.metricSubscriberEntityRepository.deleteAll(it) }
                this.subscriberEntityRepository.delete(subscriberEntity)
                true
            }
            ?: false
    }

    override fun getMetricsOfSubscriber(subscriber: Subscriber): List<Metric> {
        return this.metricSubscriberEntityRepository.findBySubscriberId(subscriber.id)
            .map { it.metricEntity!!.toMetric() }
    }
}