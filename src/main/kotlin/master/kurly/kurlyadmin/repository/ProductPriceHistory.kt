package master.kurly.kurlyadmin.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "product_price_history")
class ProductPriceHistory (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @JoinColumn(name = "pid")
    @ManyToOne
    var product: Product? = null,

    @Column(name = "datetime", nullable = false)
    var datetime: LocalDateTime = LocalDateTime.now(),

    @Column(name = "price", nullable = false)
    var price: Int = 0
){
    fun toProductPriceHistoryDto(): ProductPriceHistoryDto {
        return ProductPriceHistoryDto(
            productId = this.product!!.id!!,
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
    fun getPriceHistoryOfProduct(product: Product): List<ProductPriceHistoryDto>?
    fun getPriceHistoryOfProduct(productId: Long): List<ProductPriceHistoryDto>?
    fun getPriceHistoryOfProduct(product: Product, datetime: LocalDateTime): List<ProductPriceHistoryDto>?
    fun getPriceHistoryOfProduct(productId: Long, datetime: LocalDateTime): List<ProductPriceHistoryDto>?
}

@Repository
interface ProductPriceHistoryJpaRepository: CrudRepository<ProductPriceHistory, Long>{
    fun findByProductOrderByDatetime(product: Product): List<ProductPriceHistory>?

    fun findByProductAndDatetimeGreaterThanEqualOrderByDatetime(product: Product, datetime: LocalDateTime): List<ProductPriceHistory>?

    @Query(nativeQuery = true, value = "SELECT * FROM product_price_history WHERE pid = :id ORDER BY datetime;")
    fun findByProductIdOrderByDatetime(id: Long): List<ProductPriceHistory>?

    @Query(nativeQuery = true, value = "SELECT * FROM product_price_history WHERE pid = :id AND datetime >= :datetime ORDER BY datetime")
    fun findByProductIdAndDatetimeGreaterThanEqualOrderByDatetime(id: Long, datetime: LocalDateTime): List<ProductPriceHistory>?
}

@Repository
class ProductPriceHistoryRepositoryImpl(
    private val productPriceHistoryJpaRepository: ProductPriceHistoryJpaRepository
): ProductPriceHistoryRepository {

    override fun getPriceHistoryOfProduct(product: Product): List<ProductPriceHistoryDto>? {
        return this.productPriceHistoryJpaRepository.findByProductOrderByDatetime(product)?.map { it.toProductPriceHistoryDto() }
    }

    override fun getPriceHistoryOfProduct(productId: Long): List<ProductPriceHistoryDto>? {
        return this.productPriceHistoryJpaRepository.findByProductIdOrderByDatetime(productId)?.map { it.toProductPriceHistoryDto() }
    }

    override fun getPriceHistoryOfProduct(product: Product, datetime: LocalDateTime): List<ProductPriceHistoryDto>? {
        return this.productPriceHistoryJpaRepository.findByProductAndDatetimeGreaterThanEqualOrderByDatetime(product, datetime)
            ?.map { it.toProductPriceHistoryDto() }
    }

    override fun getPriceHistoryOfProduct(productId: Long, datetime: LocalDateTime): List<ProductPriceHistoryDto>? {
        return this.productPriceHistoryJpaRepository.findByProductIdAndDatetimeGreaterThanEqualOrderByDatetime(productId, datetime)
            ?.map { it.toProductPriceHistoryDto() }
    }
}