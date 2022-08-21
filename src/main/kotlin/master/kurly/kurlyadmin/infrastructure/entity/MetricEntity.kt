package master.kurly.kurlyadmin.infrastructure.entity

import master.kurly.kurlyadmin.domain.metric.*
import org.springframework.data.repository.CrudRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
@Table(name = "metric")
class MetricEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "nickname", nullable = false, length = 100, unique = true)
    var nickname: String = "",

    @Column(name = "name", nullable = false, length = 100)
    var name: String = "",


    @Column(name = "source_type", nullable = false)
    var sourceType: SourceType = SourceType.API,

    @Column(name = "source", nullable = false, length = 400)
    var source: String = "",


    @Column(name = "is_scheduled", nullable = false)
    var isScheduled: Boolean = false,

    @Column(name = "schedule", nullable = true, length = 100)
    var schedule: String? = null,

    @Column(name = "s3_object_key", nullable = true, length = 100)
    var s3ObjectKey: String? = null,


    @Column(name = "threshold", nullable = true)
    var threshold: Double = 0.0,

    @Column(name = "threshold_direction", nullable = true)
    var thresholdDirection: ThresholdDirection = ThresholdDirection.GreaterThanThreshold,


    @Column(name = "description", nullable = false)
    var description: String = "설명 없음",

    @Column(name = "is_available", nullable = false)
    var isAvailable: Boolean = false,


    @OneToMany(mappedBy = "metricEntity")
    var productInfo: List<ProductMetricEntity> = listOf(),

    @OneToMany(mappedBy = "metricEntity")
    var subscriberInfo: List<MetricSubscriberEntity> = listOf()
){
    fun toMetric(): Metric{
        return Metric(
            id = this.id!!,
            nickname = this.nickname,
            name = this.name,
            sourceType = this.sourceType,
            source = this.source,
            isScheduled = this.isScheduled,
            cronSchedule = this.schedule,
            s3ObjectKey = this.s3ObjectKey,
            threshold = this.threshold,
            thresholdDirection = this.thresholdDirection,
            description = this.description,
            isAvailable = this.isAvailable
        )
    }

    companion object{
        fun fromMetric(metric: Metric): MetricEntity {
            return MetricEntity(
                id = metric.id,
                nickname = metric.nickname,
                name = metric.name,
                sourceType = metric.sourceType,
                source = metric.source,
                isScheduled = metric.isScheduled,
                schedule = metric.cronSchedule,
                s3ObjectKey = metric.s3ObjectKey,
                threshold = metric.threshold,
                thresholdDirection = metric.thresholdDirection,
                description = metric.description,
                isAvailable = metric.isAvailable
            )
        }
    }
}

@Repository
interface MetricEntityRepository: CrudRepository<MetricEntity, Long>{
    fun findAllByIsAvailable(isAvailable: Boolean): List<MetricEntity>
}