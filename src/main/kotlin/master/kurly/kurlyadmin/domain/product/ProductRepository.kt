package master.kurly.kurlyadmin.domain.product

import master.kurly.kurlyadmin.domain.metric.Metric

interface ProductRepository {
    fun getAllProducts(): List<Product>
    fun findById(id: Long): Product?
    fun getMetricImportance(product: Product): Map<Metric, ProductMetricImportance>?
    fun save(product: Product): Boolean
}