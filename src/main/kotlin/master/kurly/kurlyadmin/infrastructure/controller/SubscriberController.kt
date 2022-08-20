package master.kurly.kurlyadmin.infrastructure.controller

import master.kurly.kurlyadmin.application.SubscriberService
import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.subscriber.Subscriber
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/subscriber")
class SubscriberController(
    private val subscriberService: SubscriberService
) {

    @GetMapping("/all")
    fun getAllSubscribers(): List<Subscriber> {
        return this.subscriberService.getAllSubscribers()
    }

    @GetMapping("/id")
    fun getSubscriberById(
        @RequestParam("value") id: Long
    ): Subscriber? {
        return this.subscriberService.getSubscriberById(id)
    }

    @GetMapping("/metrics")
    fun getMetricsOfSubscriber(
        @RequestParam("id") id: Long
    ): List<Metric>? {
        return this.subscriberService.getMetricsOfSubscriber(id)
    }

    @PostMapping("/create")
    fun createSubscriber(
        @RequestBody subscriber: Subscriber
    ): Boolean {
        return this.subscriberService.createSubscriber(subscriber)
    }

    @DeleteMapping("/delete")
    fun deleteSubscriberById(
        @RequestParam("id") id: Long
    ): Boolean {
        return this.subscriberService.deleteSubscriberById(id)
    }

}