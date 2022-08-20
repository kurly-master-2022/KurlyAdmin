package master.kurly.kurlyadmin.infrastructure.entity

import master.kurly.kurlyadmin.domain.product.ProductMetricImportance
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import javax.persistence.*

@Entity
@Table(name = "product_metric")
class ProductMetricEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @JoinColumn(name = "pid")
    @ManyToOne
    var productEntity: ProductEntity? = null,

    @JoinColumn(name = "mid")
    @ManyToOne
    var metricEntity: MetricEntity? = null,

    @Column(name = "preference")
    @Enumerated(EnumType.STRING)
    var preference: ProductMetricImportance = ProductMetricImportance.MEDIUM
)

@Repository
interface ProductMetricEntityRepository: CrudRepository<ProductMetricEntity, Long>{
    @Query(nativeQuery = true, value = "SELECT * FROM product_metric WHERE pid = :id")
    fun findByProductId(id: Long): List<ProductMetricEntity>

    @Query(nativeQuery = true, value = "SELECT * FROM product_metric WHERE mid = :id")
    fun findByMetricId(id: Long): List<ProductMetricEntity>

    fun findByProductEntityAndMetricEntity(productEntity: ProductEntity, metricEntity: MetricEntity): ProductMetricEntity?
}

