package master.kurly.kurlyadmin.infrastructure.controller

import master.kurly.kurlyadmin.application.MetricService
import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.metric.MetricHistory
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

    @GetMapping("/available")
    fun getAvailableMetrics(): List<Metric> {
        return this.metricService.getAvailableMetrics()
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
    ): List<MetricMappingProductsDto>? {
        return this.metricService.getProductsOfMetric(id)
            ?.map { MetricMappingProductsDto(it.key, it.value) }
    }

    @PostMapping("/create")
    fun createMetric(
        @RequestBody metricCreateDto: MetricCreateDto
    ): Boolean {
        return this.metricService.createMetric(metricCreateDto)
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
            metricSubscriberDto.metricId, metricSubscriberDto.subscriberIds
        )
    }

    @DeleteMapping("/subscriber")
    fun removeSubscriberToMetric(
        @RequestBody metricSubscriberDto: MetricSubscriberDto
    ): Boolean {
        return this.metricService.removeSubscriberToMetric(
            metricSubscriberDto.metricId, metricSubscriberDto.subscriberIds
        )
    }
}