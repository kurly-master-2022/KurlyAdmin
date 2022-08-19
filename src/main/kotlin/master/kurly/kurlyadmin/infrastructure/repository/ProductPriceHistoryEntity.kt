package master.kurly.kurlyadmin.infrastructure.repository

import org.springframework.data.jpa.repository.Query
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
){
    fun toProductPriceHistoryDto(): ProductPriceHistoryDto {
        return ProductPriceHistoryDto(
            productId = this.productEntity!!.id!!,
            datetime = this.datetime,
            price = this.price
        )
    }
}

data class ProductPriceHistoryDto(
    val productId: Long,
    val datetime: LocalDateTime,
    val price: Int
)

interface ProductPriceHistoryRepository{
    fun getPriceHistoryOfProduct(productEntity: ProductEntity): List<ProductPriceHistoryDto>?
    fun getPriceHistoryOfProduct(productId: Long): List<ProductPriceHistoryDto>?
    fun getPriceHistoryOfProduct(productEntity: ProductEntity, datetime: LocalDateTime): List<ProductPriceHistoryDto>?
    fun getPriceHistoryOfProduct(productId: Long, datetime: LocalDateTime): List<ProductPriceHistoryDto>?
}

@Repository
interface ProductPriceHistoryJpaRepository: CrudRepository<ProductPriceHistoryEntity, Long>{
    fun findByProductOrderByDatetime(productEntity: ProductEntity): List<ProductPriceHistoryEntity>?

    fun findByProductAndDatetimeGreaterThanEqualOrderByDatetime(productEntity: ProductEntity, datetime: LocalDateTime): List<ProductPriceHistoryEntity>?

    @Query(nativeQuery = true, value = "SELECT * FROM product_price_history WHERE pid = :id ORDER BY datetime;")
    fun findByProductIdOrderByDatetime(id: Long): List<ProductPriceHistoryEntity>?

    @Query(nativeQuery = true, value = "SELECT * FROM product_price_history WHERE pid = :id AND datetime >= :datetime ORDER BY datetime")
    fun findByProductIdAndDatetimeGreaterThanEqualOrderByDatetime(id: Long, datetime: LocalDateTime): List<ProductPriceHistoryEntity>?
}

@Repository
class ProductPriceHistoryRepositoryImpl(
    private val productPriceHistoryJpaRepository: ProductPriceHistoryJpaRepository
): ProductPriceHistoryRepository {

    override fun getPriceHistoryOfProduct(productEntity: ProductEntity): List<ProductPriceHistoryDto>? {
        return this.productPriceHistoryJpaRepository.findByProductOrderByDatetime(productEntity)?.map { it.toProductPriceHistoryDto() }
    }

    override fun getPriceHistoryOfProduct(productId: Long): List<ProductPriceHistoryDto>? {
        return this.productPriceHistoryJpaRepository.findByProductIdOrderByDatetime(productId)?.map { it.toProductPriceHistoryDto() }
    }

    override fun getPriceHistoryOfProduct(productEntity: ProductEntity, datetime: LocalDateTime): List<ProductPriceHistoryDto>? {
        return this.productPriceHistoryJpaRepository.findByProductAndDatetimeGreaterThanEqualOrderByDatetime(productEntity, datetime)
            ?.map { it.toProductPriceHistoryDto() }
    }

    override fun getPriceHistoryOfProduct(productId: Long, datetime: LocalDateTime): List<ProductPriceHistoryDto>? {
        return this.productPriceHistoryJpaRepository.findByProductIdAndDatetimeGreaterThanEqualOrderByDatetime(productId, datetime)
            ?.map { it.toProductPriceHistoryDto() }
    }
}