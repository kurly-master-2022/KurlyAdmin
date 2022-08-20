package master.kurly.kurlyadmin.infrastructure.repository

import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.product.Product
import master.kurly.kurlyadmin.domain.product.ProductMetricImportance
import master.kurly.kurlyadmin.domain.product.ProductRepository
import master.kurly.kurlyadmin.infrastructure.entity.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
class ProductRepositoryImpl(
    private val productEntityRepository: ProductEntityRepository,
    private val metricEntityRepository: MetricEntityRepository,
    private val productMetricEntityRepository: ProductMetricEntityRepository
): ProductRepository {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun getAllProducts(): List<Product> {
        return this.productEntityRepository.findAll().map { it.toProduct() }
    }

    override fun findById(id: Long): Product? {
        return this.productEntityRepository.findById(id).orElse(null)?.toProduct()
    }

    override fun addMetricToProduct(productId: Long, metricId: Long): Boolean {
        val product = this.productEntityRepository.findById(productId).orElse(null)
        val metric = this.metricEntityRepository.findById(metricId).orElse(null)

        return if (product != null && metric != null){
            this.productMetricEntityRepository.save(
                ProductMetricEntity(null, product, metric, ProductMetricImportance.MEDIUM)
            )
            true
        }else{
            this.logger.warn("product id, metric id 에 맞는 Product 와 Metric 의 조회에 실패했습니다!")
            false
        }
    }

    override fun removeMetricToProduct(productId: Long, metricId: Long): Boolean {
        val product = this.productEntityRepository.findById(productId).orElse(null)
        val metric = this.metricEntityRepository.findById(metricId).orElse(null)

        return if (product != null && metric != null){
            this.productMetricEntityRepository.findByProductEntityAndMetricEntity(product, metric)
                ?.let { this.productMetricEntityRepository.delete(it) }
            true
        }else{
            this.logger.warn("product id, metric id 에 맞는 Product 와 Metric 의 조회에 실패했습니다!")
            false
        }
    }

    override fun modifyMetricImportanceOfProduct(
        productId: Long,
        metricId: Long,
        importance: ProductMetricImportance
    ): Boolean {
        val product = this.productEntityRepository.findById(productId).orElse(null)
        val metric = this.metricEntityRepository.findById(metricId).orElse(null)

        return if (product != null && metric != null){
            this.productMetricEntityRepository.findByProductEntityAndMetricEntity(product, metric)
                ?.let {
                    it.preference = importance
                    this.productMetricEntityRepository.save(it)
                }
            true
        }else{
            this.logger.warn("product id, metric id 에 맞는 Product 와 Metric 의 조회에 실패했습니다!")
            false
        }
    }

    override fun getMetricImportance(product: Product): Map<Metric, ProductMetricImportance> {
        return this.productMetricEntityRepository
            .findByProductId(product.id)
            .associate { it.metricEntity!!.toMetric() to it.preference }
    }

    @Transactional
    override fun save(product: Product): Boolean {
        val saveEntity = this.productEntityRepository.findById(product.id).orElse(null)
            ?.let {
                it.update(product)
                it
            }
            ?: ProductEntity.fromProduct(product)
        this.productEntityRepository.save(saveEntity)
        return true
    }
}