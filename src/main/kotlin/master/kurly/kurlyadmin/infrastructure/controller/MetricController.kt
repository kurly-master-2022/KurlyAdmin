package master.kurly.kurlyadmin.infrastructure.controller

import master.kurly.kurlyadmin.application.MetricService
import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.metric.MetricHistory
import master.kurly.kurlyadmin.domain.product.Product
import master.kurly.kurlyadmin.domain.subscriber.Subscriber
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/metric")
class MetricController(
    private val metricService: MetricService
) {

    @GetMapping("/all")
    fun getAllMetrics(): List<Metric> {
        return this.metricService.getAllMetrics()
    }

    @GetMapping("/id")
    fun getById(
        @RequestParam("value") id: Long
    ): Metric? {
        return this.metricService.getMetricById(id)
    }

    @GetMapping("/history")
    fun getMetricHistory(
        @RequestParam("id") id: Long,
        @RequestParam("start") start: String,
        @RequestParam("end") end: String
    ): MetricHistory? {
        return this.metricService.getMetricHistory(id, start, end)
    }

    @GetMapping("/subscribers")
    fun getSubscribersOfMetric(
        @RequestParam("id") id: Long
    ): List<Subscriber>? {
        return this.metricService.getSubscribersOfMetric(id)
    }

    @GetMapping("/products")
    fun getProductsOfMetric(
        @RequestParam("id") id: Long
    ): List<Product>? {
        return this.metricService.getProductsOfMetric(id)
    }

    @PostMapping("/create")
    fun createMetric(
        // 규격에 맞는 것 만들어야함.
    ){
        TODO()
        // this.metricService.createMetric()
    }

    @DeleteMapping("/delete")
    fun deleteMetricById(
        @RequestParam("id") id: Long
    ): Boolean {
        return this.metricService.deleteMetricById(id)
    }

    @PostMapping("/subscriber")
    fun addSubscriberToMetric(
        @RequestBody metricSubscriberDto: MetricSubscriberDto
    ): Boolean {
        return this.metricService.addSubscriberToMetric(
            metricSubscriberDto.metricId, metricSubscriberDto.subscriberId
        )
    }

    @DeleteMapping("/subscriber")
    fun removeSubscriberToMetric(
        @RequestBody metricSubscriberDto: MetricSubscriberDto
    ): Boolean {
        return this.metricService.removeSubscriberToMetric(
            metricSubscriberDto.metricId, metricSubscriberDto.subscriberId
        )
    }
}