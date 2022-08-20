package master.kurly.kurlyadmin.infrastructure.entity

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.*


@Entity
@Table(name = "metric_subscriber")
class MetricSubscriberEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @JoinColumn(name = "mid")
    @ManyToOne
    var metricEntity: MetricEntity? = null,

    @JoinColumn(name = "sid")
    @ManyToOne
    var subscriberEntity: SubscriberEntity? = null
)

@Repository
interface MetricSubscriberEntityRepository: CrudRepository<MetricSubscriberEntity, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM metric_subscriber WHERE mid = :id")
    fun findByMetricId(id: Long): List<MetricSubscriberEntity>

    @Query(nativeQuery = true, value = "SELECT * FROM metric_subscriber WHERE sid = :id")
    fun findBySubscriberId(id: Long): List<MetricSubscriberEntity>

    fun findByMetricEntityAndSubscriberEntity(metricEntity: MetricEntity, subscriberEntity: SubscriberEntity): MetricSubscriberEntity?
}