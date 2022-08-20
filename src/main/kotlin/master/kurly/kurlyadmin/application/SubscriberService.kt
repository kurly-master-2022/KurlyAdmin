package master.kurly.kurlyadmin.application

import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.subscriber.Subscriber
import master.kurly.kurlyadmin.domain.subscriber.SubscriberRepository
import org.springframework.stereotype.Service

@Service
class SubscriberService(
    private val subscriberRepository: SubscriberRepository
) {

    fun getAllSubscribers(): List<Subscriber> {
        return this.subscriberRepository.getAllSubscribers()
    }

    fun getSubscriberById(id: Long): Subscriber? {
        return this.subscriberRepository.getSubscriberById(id)
    }

    fun getMetricsOfSubscriber(subscriberId: Long): List<Metric>? {
        return this.getSubscriberById(subscriberId)
            ?.let { this.subscriberRepository.getMetricsOfSubscriber(it) }
    }

    fun createSubscriber(subscriber: Subscriber): Boolean {
        return this.subscriberRepository.createSubscriber(subscriber)
    }

    fun deleteSubscriberById(subscriberId: Long): Boolean {
        return this.subscriberRepository.deleteSubscriber(subscriberId)
    }
}