package master.kurly.kurlyadmin.infrastructure.repository

import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.subscriber.Subscriber
import master.kurly.kurlyadmin.domain.subscriber.SubscriberRepository
import master.kurly.kurlyadmin.infrastructure.entity.MetricSubscriberEntityRepository
import master.kurly.kurlyadmin.infrastructure.entity.SubscriberEntity
import master.kurly.kurlyadmin.infrastructure.entity.SubscriberEntityRepository
import org.springframework.stereotype.Repository

@Repository
class SubscriberRepositoryImpl(
    private val subscriberEntityRepository: SubscriberEntityRepository,
    private val metricSubscriberEntityRepository: MetricSubscriberEntityRepository
): SubscriberRepository {
    override fun getAllSubscribers(): List<Subscriber> {
        return this.subscriberEntityRepository.findAll().map { it.toSubscriber() }
    }

    override fun getSubscriberById(id: Long): Subscriber? {
        return this.subscriberEntityRepository.findById(id).orElse(null)?.toSubscriber()
    }

    override fun createSubscriber(subscriber: Subscriber): Boolean {
        SubscriberEntity.newSubscriber(subscriber)
            .let { this.subscriberEntityRepository.save(it) }
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