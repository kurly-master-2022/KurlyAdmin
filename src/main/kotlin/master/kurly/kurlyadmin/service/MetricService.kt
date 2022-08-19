package master.kurly.kurlyadmin.service

import master.kurly.kurlyadmin.old_controller.NewMetricDto
import master.kurly.kurlyadmin.repository.MetricDto
import master.kurly.kurlyadmin.repository.MetricHistoryRepository
import master.kurly.kurlyadmin.repository.MetricRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MetricService(
    private val metricRepository: MetricRepository,
    private val metricHistoryRepository: MetricHistoryRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getAllMetrics(): List<MetricDto> {
        return this.metricRepository.findAll()
    }

    fun getMetricByName(name: String): MetricDto? {
        return this.metricRepository.findByName(name)
    }

    fun getMetricById(id: Long): MetricDto? {
        return this.metricRepository.findByIdentifier(id)
    }

    fun getMetricHistory(metricId: Long): MetricHistoryDto? {
        return this.metricHistoryRepository.getMetricHistory(metricId)
            ?.fold(MetricHistoryDto(mutableListOf(), mutableListOf())) { default, acc ->
                default.apply {
                    this.datetimes.add(acc.datetime)
                    this.amounts.add(acc.amount)
                }
            }
    }

    fun getMetricHistory(metricId: Long, datetime: LocalDateTime): MetricHistoryDto? {
        return this.metricHistoryRepository.getMetricHistory(metricId, datetime)
            ?.fold(MetricHistoryDto(mutableListOf(), mutableListOf())) { default, acc ->
                default.apply {
                    this.datetimes.add(acc.datetime)
                    this.amounts.add(acc.amount)
                }
            }
    }

    fun addNewMetric(newMetricDto: NewMetricDto): Boolean{
        this.logger.warn("구현방법 생각해내야 함")
        return false
    }

}

data class MetricHistoryDto(
    val datetimes: MutableList<LocalDateTime>,
    val amounts: MutableList<Double>
)