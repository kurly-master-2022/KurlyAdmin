package master.kurly.kurlyadmin.infrastructure.entity

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "product_price_history")
class ProductPriceHistoryEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @JoinColumn(name = "pid")
    @ManyToOne
    var productEntity: ProductEntity? = null,

    @Column(name = "datetime", nullable = false)
    var datetime: LocalDateTime = LocalDateTime.now(),

    @Column(name = "price", nullable = false)
    var price: Int = 0
)

@Repository
interface ProductPriceHistoryEntityRepository: CrudRepository<ProductPriceHistoryEntity, Long>{

    fun findByProductEntityAndDatetimeGreaterThanEqualAndDatetimeLessThanEqual(
        productEntity: ProductEntity,
        datetime: LocalDateTime,
        datetime2: LocalDateTime
    ): List<ProductPriceHistoryEntity>
}