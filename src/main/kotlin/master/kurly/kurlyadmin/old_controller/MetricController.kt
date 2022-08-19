package master.kurly.kurlyadmin.old_controller

import master.kurly.kurlyadmin.repository.MetricDto
import master.kurly.kurlyadmin.service.MetricHistoryDto
import master.kurly.kurlyadmin.service.MetricService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/metric")
class MetricController(
    private val metricService: MetricService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/all")
    fun getAllMetrics(): List<MetricDto> {
        return this.metricService.getAllMetrics()
    }

    @GetMapping("/name")
    fun getMetricByName(
        @RequestParam("value") name: String
    ): MetricDto? {
        return this.metricService.getMetricByName(name)
    }

    @GetMapping("/id")
    fun getMetricById(
        @RequestParam("value") id: Long
    ): MetricDto? {
        return this.metricService.getMetricById(id)
    }

    @GetMapping("/history")
    fun getAmountHistory(
        @RequestParam("id") id: Long,
        @RequestParam("datetime") datetime: String?
    ): MetricHistoryDto? {
        return datetime?.let { LocalDateTime.parse(it) }
            ?.let { this.metricService.getMetricHistory(id, it) }
            ?: this.metricService.getMetricHistory(id)
    }

    @PostMapping("/new")
    fun addNewMetric(
        @RequestBody newMetricDto: NewMetricDto
    ): Boolean {
        return this.metricService.addNewMetric(newMetricDto)
    }

}