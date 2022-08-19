package master.kurly.kurlyadmin.infrastructure.repository

import master.kurly.kurlyadmin.domain.metric.*
import master.kurly.kurlyadmin.domain.product.Product
import master.kurly.kurlyadmin.domain.subscriber.Subscriber
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
@Table(name = "metric")
class MetricEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name", nullable = false, length = 100)
    var name: String = "",

    @Column(name = "source_type", nullable = false)
    var sourceType: SourceType = SourceType.API,

    @Column(name = "source", nullable = false, length = 400)
    var source: String = "",

    @Column(name = "schedule", nullable = true, length = 100)
    var schedule: String? = null,

    @Column(name = "s3_object_key", nullable = true, length = 100)
    var s3ObjectKey: String? = null,

    @Column(name = "threshold", nullable = true)
    var threshold: Double = 0.0,

    @Column(name = "threshold_direction", nullable = true)
    var thresholdDirection: Boolean = false,

    @Column(name = "is_available", nullable = false)
    var isAvailable: Boolean = false,

    @OneToMany(mappedBy = "metricEntity")
    var productInfo: List<ProductMetricEntity> = listOf()
){
    fun toMetric(): Metric{
        return Metric(
            id = this.id!!,
            name = this.name,
            sourceType = this.sourceType,
            source = this.source,
            cronSchedule = this.schedule,
            s3ObjectKey = this.s3ObjectKey,
            threshold = this.threshold,
            thresholdDirection = ThresholdDirection.find(this.thresholdDirection)
        )
    }
}

@Repository
interface MetricEntityRepository: CrudRepository<MetricEntity, Long>

@Repository
class MetricRepositoryImpl(
    private val metricEntityRepository: MetricEntityRepository,
    private val productMetricEntityRepository: ProductMetricEntityRepository,
): MetricRepository{
    override fun getAllMetrics(): List<Metric> {
        return this.metricEntityRepository.findAll().map { it.toMetric() }
    }

    override fun getProductsOfMetric(metric: Metric): List<Product>? {
        return this.metricEntityRepository.findById(metric.id).orElse(null)
            ?.let { metricEntity ->
                this.productMetricEntityRepository.findByMetricId(metricEntity.id!!)
                    .map { it.productEntity!!.toProduct() }
            }
    }

    override fun getSubscribersOfMetric(metric: Metric): List<Subscriber>? {
        TODO("Not yet implemented")
    }

    override fun isAlarmAvailable(metric: Metric): Boolean {
        return this.metricEntityRepository.findById(metric.id).get().isAvailable
    }
}