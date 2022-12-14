package master.kurly.kurlyadmin.infrastructure.entity

import master.kurly.kurlyadmin.domain.product.Product
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
@Table(name = "product")
class ProductEntity (

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

    @OneToMany(mappedBy = "productEntity")
    var metricInfo: List<ProductMetricEntity> = listOf()
){
    fun toProduct(): Product{
        return Product(
            id = this.id!!,
            name = this.name,
            price = this.price,
            description = this.description,
            link = this.link,
            thumbnail = this.thumbnail
        )
    }

    fun update(product: Product){
        this.name = product.name
        this.price = product.price
        this.description = product.description
        this.link = product.link
        this.thumbnail = product.thumbnail
    }

    companion object{
        fun fromProduct(product: Product): ProductEntity {
            return ProductEntity(
                id = product.id,
                name = product.name,
                price = product.price,
                description = product.description,
                link = product.link,
                thumbnail = product.thumbnail
            )
        }
    }
}

@Repository
interface ProductEntityRepository: CrudRepository<ProductEntity, Long>