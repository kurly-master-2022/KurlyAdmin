package master.kurly.kurlyadmin.application

import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.subscriber.Subscriber
import master.kurly.kurlyadmin.domain.subscriber.SubscriberRepository
import master.kurly.kurlyadmin.infrastructure.controller.CreateSubscriberDto
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

    fun createSubscriber(subscriberInfo: CreateSubscriberDto): Boolean {
        Subscriber(
            id = -1,
            name = subscriberInfo.name,
            subscribeType = subscriberInfo.type,
            uri = subscriberInfo.uri
        ).let {
            return this.subscriberRepository.createSubscriber(it)
        }
    }

    fun deleteSubscriberById(subscriberId: Long): Boolean {
        return this.subscriberRepository.deleteSubscriber(subscriberId)
    }
}