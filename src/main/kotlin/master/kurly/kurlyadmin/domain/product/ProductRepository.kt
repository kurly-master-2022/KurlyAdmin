package master.kurly.kurlyadmin.domain.product

import master.kurly.kurlyadmin.domain.metric.Metric
import java.time.LocalDateTime

interface ProductRepository {
    fun getAllProducts(): List<Product>
    fun findById(id: Long): Product?
    fun addMetricToProduct(productId: Long, metricIds: List<Long>): Boolean
    fun removeMetricToProduct(productId: Long, metricIds: List<Long>): Boolean
    fun modifyMetricImportanceOfProduct(productId: Long, metricId: Long, importance: ProductMetricImportance): Boolean
    fun getMetricImportance(product: Product): Map<Metric, ProductMetricImportance>
    fun changeProductPrice(product: Product): Boolean
    fun getProductPriceHistory(productId: Long, startAt: LocalDateTime, endAt: LocalDateTime): ProductHistory?
}