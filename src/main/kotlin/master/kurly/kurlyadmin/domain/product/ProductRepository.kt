package master.kurly.kurlyadmin.domain.product

import master.kurly.kurlyadmin.domain.metric.Metric

interface ProductRepository {
    fun getAllProducts(): List<Product>
    fun findById(id: Long): Product?
    fun addMetricToProduct(productId: Long, metricId: Long): Boolean
    fun removeMetricToProduct(productId: Long, metricId: Long): Boolean
    fun modifyMetricImportanceOfProduct(productId: Long, metricId: Long, importance: ProductMetricImportance): Boolean
    fun getMetricImportance(product: Product): Map<Metric, ProductMetricImportance>
    fun save(product: Product): Boolean

    // TODO
    // fun productHistory()
}