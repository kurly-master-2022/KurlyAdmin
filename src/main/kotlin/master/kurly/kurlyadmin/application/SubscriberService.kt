package master.kurly.kurlyadmin.application

import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.metric.MetricRepository
import master.kurly.kurlyadmin.domain.subscriber.Subscriber
import master.kurly.kurlyadmin.domain.subscriber.SubscriberAlarmManager
import master.kurly.kurlyadmin.domain.subscriber.SubscriberRepository
import master.kurly.kurlyadmin.infrastructure.controller.CreateSubscriberDto
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class SubscriberService(
    private val subscriberRepository: SubscriberRepository,
    private val metricRepository: MetricRepository,
    private val subscriberAlarmManager: SubscriberAlarmManager
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

    @Transactional
    fun createSubscriber(subscriberInfo: CreateSubscriberDto) {
        Subscriber(
            id = -1,
            name = subscriberInfo.name,
            subscribeType = subscriberInfo.type,
            uri = subscriberInfo.uri
        ).let { subscriber ->
            this.subscriberRepository.createSubscriber(subscriber, subscriberInfo.metricIds)
            this.subscriberAlarmManager.addSubscriber(
                subscriber,
                // 좋은 구현은 아니지만... 시간이 없다.
                subscriberInfo.metricIds.mapNotNull { this.metricRepository.getMetricById(it) }
            )
        }
    }

    @Transactional
    fun deleteSubscriberById(subscriberId: Long, arn: String): Boolean {
        this.subscriberRepository.getSubscriberById(subscriberId)
            ?.let {
                this.subscriberAlarmManager.deleteSubscriber(it, arn)
            }
        return this.subscriberRepository.deleteSubscriber(subscriberId)
    }
}