package master.kurly.kurlyadmin.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
@Table(name = "metric")
class Metric (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name", nullable = false, length = 100)
    var name: String = "",

    @Column(name = "source_type", nullable = false)
    var sourceType: String = "",

    @Column(name = "source", nullable = false, length = 400)
    var source: String = "",

    @Column(name = "schedule", nullable = true, length = 100)
    var schedule: String? = null,

    @Column(name = "s3_object_key", nullable = true, length = 100)
    var s3ObjectKey: String? = null,

    @Column(name = "threshold", nullable = true)
    var threshold: Double? = 0.0,

    @Column(name = "threshold_direction", nullable = true)
    var thresholdDirection: Boolean? = false,

    @Column(name = "is_available", nullable = false)
    var isAvailable: Boolean = false,

    @OneToMany(mappedBy = "metric")
    var productInfo: List<ProductMetric> = listOf()
){

    fun toMetricDto(): MetricDto {
        return MetricDto(
            id ?: 0,
            name,
            sourceType,
            source,
            schedule,
            s3ObjectKey,
            threshold,
            thresholdDirection,
            isAvailable,
            this.productInfo.map { it.toProductByMetricDto() }
        )
    }

    fun toMetricDtoWithoutProduct(): MetricDto {
        return MetricDto(
            id ?: 0,
            name,
            sourceType,
            source,
            schedule,
            s3ObjectKey,
            threshold,
            thresholdDirection,
            isAvailable,
            null
        )
    }
}

data class MetricDto(
    var id: Long,
    var name: String,
    var sourceType: String,
    var source: String,
    var schedule: String?,
    var s3ObjectKey: String?,
    var threshold: Double?,
    var thresholdDirection: Boolean?,
    var isAvailable: Boolean,
    var products: List<ProductByMetricDto>?
)

interface MetricRepository {
    fun findByIdentifier(id: Long): MetricDto?
    fun findAll(): List<MetricDto>
    fun findByName(name: String): MetricDto?
}

@Repository
interface MetricJpaRepository: CrudRepository<Metric, Long>{
    fun findByName(name: String): Metric?
}

@Repository
class MetricRepositoryImpl(
    private val repository: MetricJpaRepository
): MetricRepository {
    override fun findByIdentifier(id: Long): MetricDto? {
        return this.repository.findById(id).orElse(null)?.toMetricDto()
    }

    override fun findAll(): List<MetricDto> {
        return this.repository.findAll().map { it.toMetricDtoWithoutProduct() }
    }

    override fun findByName(name: String): MetricDto? {
        return this.repository.findByName(name)?.toMetricDto()
    }
}