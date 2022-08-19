package master.kurly.kurlyadmin.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "metric_history")
class MetricHistory (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @JoinColumn(name = "mid")
    @ManyToOne
    var metric: Metric? = null,

    @Column(name = "datetime", nullable = false)
    var datetime: LocalDateTime = LocalDateTime.now(),

    @Column(name = "amount", nullable = false)
    var amount: Double = 0.0
){
    fun toMetricHistoryDto(): MetricHistoryDto {
        return MetricHistoryDto(
            metricId = this.metric!!.id!!,
            datetime = this.datetime,
            amount = this.amount
        )
    }
}

data class MetricHistoryDto(
    val metricId: Long,
    val datetime: LocalDateTime,
    val amount: Double
)

interface MetricHistoryRepository{
    fun getMetricHistory(metric: Metric): List<MetricHistoryDto>?
    fun getMetricHistory(metricId: Long): List<MetricHistoryDto>?
    fun getMetricHistory(metric: Metric, datetime: LocalDateTime): List<MetricHistoryDto>?
    fun getMetricHistory(metricId: Long, datetime: LocalDateTime): List<MetricHistoryDto>?
}

@Repository
interface MetricHistoryJpaRepository: CrudRepository<MetricHistory, Long>{
    fun findByMetricOrderByDatetime(metric: Metric): List<MetricHistory>?

    fun findByMetricAndDatetimeGreaterThanEqualOrderByDatetime(metric: Metric, datetime: LocalDateTime): List<MetricHistory>?

    @Query(nativeQuery = true, value = "SELECT * FROM metric_history WHERE mid = :id ORDER BY datetime;")
    fun findByMetricIdOrderByDatetime(id: Long): List<MetricHistory>?

    @Query(nativeQuery = true, value = "SELECT * FROM metric_history WHERE mid = :id AND datetime >= :datetime ORDER BY datetime")
    fun findByMetricIdAndDatetimeGreaterThanEqualOrderByDatetime(id: Long, datetime: LocalDateTime): List<MetricHistory>?
}

@Repository
class MetricHistoryRepositoryImpl(
    private val metricHistoryJpaRepository: MetricHistoryJpaRepository
): MetricHistoryRepository {

    override fun getMetricHistory(metric: Metric): List<MetricHistoryDto>? {
        return this.metricHistoryJpaRepository.findByMetricOrderByDatetime(metric)?.map { it.toMetricHistoryDto() }
    }

    override fun getMetricHistory(metricId: Long): List<MetricHistoryDto>? {
        return this.metricHistoryJpaRepository.findByMetricIdOrderByDatetime(metricId)?.map { it.toMetricHistoryDto() }
    }

    override fun getMetricHistory(metric: Metric, datetime: LocalDateTime): List<MetricHistoryDto>? {
        return this.metricHistoryJpaRepository.findByMetricAndDatetimeGreaterThanEqualOrderByDatetime(metric, datetime)
            ?.map { it.toMetricHistoryDto() }
    }

    override fun getMetricHistory(metricId: Long, datetime: LocalDateTime): List<MetricHistoryDto>? {
        return this.metricHistoryJpaRepository.findByMetricIdAndDatetimeGreaterThanEqualOrderByDatetime(metricId, datetime)
            ?.map { it.toMetricHistoryDto() }
    }

}