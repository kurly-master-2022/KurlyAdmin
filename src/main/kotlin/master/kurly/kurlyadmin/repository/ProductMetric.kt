package master.kurly.kurlyadmin.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import javax.persistence.*
import javax.transaction.Transactional

enum class Importance{
    VERY_LOW, LOW, MEDIUM, HIGH, VERY_HIGH;
}

@Entity
@Table(name = "product_metric")
class ProductMetric (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @JoinColumn(name = "pid")
    @ManyToOne
    var product: Product? = null,

    @JoinColumn(name = "mid")
    @ManyToOne
    var metric: Metric? = null,

    @Column(name = "preference")
    @Enumerated(EnumType.STRING)
    var preference: Importance = Importance.MEDIUM
){

    fun toMetricByProductDto(): MetricByProductDto {
        return MetricByProductDto(
            id = this.id ?: 0,
            metric = this.metric!!.toMetricDtoWithoutProduct(),
            preference = preference
        )
    }

    fun toProductByMetricDto(): ProductByMetricDto {
        return ProductByMetricDto(
            id = this.id ?: 0,
            product = this.product!!.toProductDtoWithoutMetric(),
            preference = preference
        )
    }
}

data class MetricByProductDto(
    val id: Long,
    val metric: MetricDto?,
    val preference: Importance
)

data class ProductByMetricDto(
    val id: Long,
    val product: ProductDto,
    val preference: Importance
)

interface ProductMetricRepository {
    fun getMetricsOfProduct(productId: Long): List<MetricByProductDto>
    fun getProductsOfMetric(metricId: Long): List<ProductByMetricDto>
    fun addMetricToProduct(productId: Long, metricId: Long): Boolean
    fun deleteMetricToProduct(productId: Long, metricId: Long): Boolean
    fun modifyMetricOfProduct(productId: Long, metricId: Long, importance: Importance): Boolean
}

@Repository
interface ProductMetricJpaRepository: CrudRepository<ProductMetric, Long>{
    @Query(nativeQuery = true, value = "SELECT * FROM product_metric WHERE pid = :id")
    fun findByProductId(id: Long): List<ProductMetric>

    @Query(nativeQuery = true, value = "SELECT * FROM product_metric WHERE mid = :id")
    fun findByMetricId(id: Long): List<ProductMetric>

    fun findByProductAndMetric(product: Product, metric: Metric): ProductMetric?
}

@Repository
class ProductMetricRepositoryImpl(
    private val productMetricJpaRepository: ProductMetricJpaRepository,
    private val productJpaRepository: ProductJpaRepository,
    private val metricJpaRepository: MetricJpaRepository
): ProductMetricRepository{

    override fun getMetricsOfProduct(productId: Long): List<MetricByProductDto> {
        return this.productMetricJpaRepository.findByProductId(productId).map { it.toMetricByProductDto() }
    }

    override fun getProductsOfMetric(metricId: Long): List<ProductByMetricDto> {
        return this.productMetricJpaRepository.findByMetricId(metricId).map { it.toProductByMetricDto() }
    }

    @Transactional
    override fun addMetricToProduct(productId: Long, metricId: Long): Boolean {
        val product = this.productJpaRepository.findById(productId).orElse(null)
        val metric = this.metricJpaRepository.findById(metricId).orElse(null)

        return if (product != null && metric != null){
            this.productMetricJpaRepository.findByProductAndMetric(product, metric)
                ?.let { false }
                ?: run {
                    this.productMetricJpaRepository.save(ProductMetric(
                        id = null,
                        product = product,
                        metric = metric,
                        preference = Importance.MEDIUM
                    ))
                    true
                }
        }else{
            false
        }
    }

    @Transactional
    override fun deleteMetricToProduct(productId: Long, metricId: Long): Boolean {
        val product = this.productJpaRepository.findById(productId).orElse(null)
        val metric = this.metricJpaRepository.findById(metricId).orElse(null)

        return if (product != null && metric != null){
            this.productMetricJpaRepository.findByProductAndMetric(product, metric)
                ?.let {
                    this.productMetricJpaRepository.delete(it)
                    true }
                ?: false
        }else{
            false
        }
    }

    @Transactional
    override fun modifyMetricOfProduct(productId: Long, metricId: Long, importance: Importance): Boolean {
        val product = this.productJpaRepository.findById(productId).orElse(null)
        val metric = this.metricJpaRepository.findById(metricId).orElse(null)

        return if (product != null && metric != null){
            this.productMetricJpaRepository.findByProductAndMetric(product, metric)
                ?.let {
                    it.preference = importance
                    this.productMetricJpaRepository.save(it)
                    true }
                ?: false
        }else{
            false
        }
    }


}

