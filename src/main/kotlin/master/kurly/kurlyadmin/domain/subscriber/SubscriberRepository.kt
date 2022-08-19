package master.kurly.kurlyadmin.domain.subscriber

import master.kurly.kurlyadmin.domain.metric.Metric

interface SubscriberRepository {
    fun getMetricsOfSubscriber(subscriber: Subscriber): List<Metric>?
}