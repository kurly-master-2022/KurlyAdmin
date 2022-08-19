package master.kurly.kurlyadmin.domain.subscriber

import master.kurly.kurlyadmin.domain.metric.Metric

enum class SubscribeType{
    EMAIL, SMS
}

data class Subscriber(
    val id: Long,
    val name: String,
    val subscribeType: SubscribeType
){
    fun getSubscribedMetric(
        subscriberRepository: SubscriberRepository
    ): List<Metric>?{
        return subscriberRepository.getMetricsOfSubscriber(this)
    }
}