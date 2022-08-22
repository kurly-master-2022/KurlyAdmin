package master.kurly.kurlyadmin.infrastructure.implementation

import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.product.Product
import master.kurly.kurlyadmin.domain.product.ProductHistory
import master.kurly.kurlyadmin.domain.product.ProductMetricImportance
import master.kurly.kurlyadmin.domain.product.ProductRepository
import master.kurly.kurlyadmin.infrastructure.entity.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.transaction.Transactional

@Repository
class ProductRepositoryImpl(
    private val productEntityRepository: ProductEntityRepository,
    private val metricEntityRepository: MetricEntityRepository,
    private val productMetricEntityRepository: ProductMetricEntityRepository,
    private val productPriceHistoryEntityRepository: ProductPriceHistoryEntityRepository
): ProductRepository {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun getAllProducts(): List<Product> {
        return this.productEntityRepository.findAll().map { it.toProduct() }
    }

    override fun findById(id: Long): Product? {
        return this.productEntityRepository.findById(id).orElse(null)?.toProduct()
    }

    @Transactional
    override fun addMetricToProduct(productId: Long, metricIds: List<Long>): Boolean {
        val product = this.productEntityRepository.findById(productId).orElse(null)
        val metrics = this.metricEntityRepository.findAllById(metricIds)

        if (product == null){
            this.logger.warn("주어진 product id 에 해당하는 Product 를 찾지 못했습니다. 작업을 실행하지 않습니다.")
            return false
        }

        if (metrics.count() != metricIds.size){
            this.logger.warn("주어진 메트릭 id 들이 유효하지 않습니다. 작업을 실행하지 않습니다.")
            return false
        }

        metrics
            .forEach { metricEntity ->
                if (this.productMetricEntityRepository.findByProductEntityAndMetricEntity(product, metricEntity) == null){
                    ProductMetricEntity(null, product, metricEntity, ProductMetricImportance.MEDIUM)
                        .let { this.productMetricEntityRepository.save(it) }
                }
            }

        return true
    }

    @Transactional
    override fun removeMetricToProduct(productId: Long, metricIds: List<Long>): Boolean {
        val product = this.productEntityRepository.findById(productId).orElse(null)
        val metrics = this.metricEntityRepository.findAllById(metricIds)

        if (product == null){
            this.logger.warn("주어진 product id 에 해당하는 Product 를 찾지 못했습니다. 작업을 실행하지 않습니다.")
            return false
        }

        if (metrics.count() != metricIds.size){
            this.logger.warn("주어진 메트릭 id 들이 유효하지 않습니다. 작업을 실행하지 않습니다.")
            return false
        }

        metrics
            .mapNotNull { this.productMetricEntityRepository.findByProductEntityAndMetricEntity(product, it) }
            .let { this.productMetricEntityRepository.deleteAll(it) }

        return true
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

    override fun changeProductPrice(product: Product): Boolean {
        val saveEntity = this.productEntityRepository.findById(product.id).orElse(null)
            ?.let {
                it.update(product)
                it
            }
            ?: ProductEntity.fromProduct(product)

        this.productEntityRepository.save(saveEntity)
        this.productPriceHistoryEntityRepository.save(ProductPriceHistoryEntity(
            null, saveEntity, LocalDateTime.now(), saveEntity.price
        ))
        return true
    }

    override fun getProductPriceHistory(
        productId: Long,
        startAt: LocalDateTime,
        endAt: LocalDateTime
    ): ProductHistory? {
        return this.productEntityRepository.findById(productId).orElse(null)
            ?.let { productEntity ->
                this.productPriceHistoryEntityRepository.findByProductEntityAndDatetimeGreaterThanEqualAndDatetimeLessThanEqual(
                    productEntity, startAt, endAt
                ).let { productPriceHistories ->
                    ProductHistory(
                        productEntity.toProduct(),
                        productPriceHistories.map { it.datetime },
                        productPriceHistories.map { it.price }
                    )
                }
            }
    }
}