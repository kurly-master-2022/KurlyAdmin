package master.kurly.kurlyadmin.domain.subscriber

import master.kurly.kurlyadmin.domain.metric.Metric

interface SubscriberRepository {
    fun getAllSubscribers(): List<Subscriber>
    fun getSubscriberById(id: Long): Subscriber?
    fun createSubscriber(subscriber: Subscriber): Boolean
    fun deleteSubscriber(subscriberId: Long): Boolean
    fun getMetricsOfSubscriber(subscriber: Subscriber): List<Metric>
}