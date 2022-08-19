package master.kurly.kurlyadmin.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.*
import javax.transaction.Transactional

@Entity
@Table(name = "product")
class Product (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name", nullable = false, length = 100)
    var name: String = "",

    @Column(name = "price", nullable = false)
    var price: Int = 0,

    @Column(name = "description", nullable = false, length = 400)
    var description: String = "",

    @Column(name = "link", nullable = false, length = 100)
    var link: String = "",

    @Column(name = "thumbnail", nullable = false, length = 100)
    var thumbnail: String = "",

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "product")
    var metricInfo: List<ProductMetric> = listOf()
){

    fun toProductDto(): ProductDto {
        return ProductDto(
            id = this.id ?: 0,
            name = this.name,
            price = this.price,
            description = this.description,
            link = this.link,
            thumbnail = this.thumbnail,
            updatedAt = this.updatedAt,
            createdAt = this.createdAt,
            metrics = this.metricInfo.map { it.toMetricByProductDto() }
        )
    }

    fun toProductDtoWithoutMetric(): ProductDto {
        return ProductDto(
            id = this.id ?: 0,
            name = this.name,
            price = this.price,
            description = this.description,
            link = this.link,
            thumbnail = this.thumbnail,
            updatedAt = this.updatedAt,
            createdAt = this.createdAt,
            metrics = null
        )
    }
}

data class ProductDto(
    var id: Long,
    var name: String,
    var price: Int,
    var description: String,
    var link: String,
    var thumbnail: String,
    var updatedAt: LocalDateTime,
    var createdAt: LocalDateTime,
    var metrics: List<MetricByProductDto>?
)

interface ProductRepository {
    fun findByIdentifier(id: Long): ProductDto?
    fun findAll(): List<ProductDto>
    fun findByName(name: String): ProductDto?
    fun changeProductPrice(productId: Long, price: Int)
}

@Repository
interface ProductJpaRepository: CrudRepository<Product, Long>{
    fun findByName(name: String): Product?
}

@Repository
class ProductRepositoryImpl(
    private val repository: ProductJpaRepository
): ProductRepository {
    override fun findByIdentifier(id: Long): ProductDto? {
        return this.repository.findById(id).orElse(null)?.toProductDto()
    }

    override fun findAll(): List<ProductDto> {
        return this.repository.findAll().map { it.toProductDtoWithoutMetric() }
    }

    override fun findByName(name: String): ProductDto? {
        return this.repository.findByName(name)?.toProductDto()
    }

    @Transactional
    override fun changeProductPrice(productId: Long, price: Int) {
        this.repository.findById(productId).orElse(null)
            ?.let { it.price = price }
    }
}