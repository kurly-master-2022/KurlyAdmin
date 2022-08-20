package master.kurly.kurlyadmin.infrastructure.entity

import master.kurly.kurlyadmin.domain.subscriber.SubscribeType
import master.kurly.kurlyadmin.domain.subscriber.Subscriber
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
@Table(name = "subscriber")
class SubscriberEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name", nullable = false)
    val name: String = "",

    @Column(name = "type", nullable = false)
    val subscribeType: SubscribeType = SubscribeType.ALL,

    @Column(name = "email", nullable = false)
    val email: String = "",

    @Column(name = "phone", nullable = false)
    val phoneNumber: String = "",

    @OneToMany(mappedBy = "subscriberEntity")
    var metricInfo: List<MetricSubscriberEntity> = listOf()

){
    fun toSubscriber(): Subscriber {
        return Subscriber(
            id = this.id!!,
            name = this.name,
            subscribeType = this.subscribeType,
            email = this.email,
            phoneNumber = this.phoneNumber
        )
    }

    companion object{
        fun fromSubscriber(subscriber: Subscriber): SubscriberEntity{
            return SubscriberEntity(
                id = subscriber.id,
                name = subscriber.name,
                subscribeType = subscriber.subscribeType,
                email = subscriber.email,
                phoneNumber = subscriber.phoneNumber
            )
        }
    }
}

@Repository
interface SubscriberEntityRepository: CrudRepository<SubscriberEntity, Long>