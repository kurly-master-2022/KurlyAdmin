package master.kurly.kurlyadmin.application

import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.product.Product
import master.kurly.kurlyadmin.domain.product.ProductMetricImportance
import master.kurly.kurlyadmin.domain.product.ProductRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository
) {
    fun getAllProducts(): List<Product> {
        return this.productRepository.getAllProducts()
    }

    fun getProductById(id: Long): Product? {
        return this.productRepository.findById(id)
    }

    fun getMetricsOfProductById(id: Long): Map<Metric, ProductMetricImportance>? {
        return this.getProductById(id)
            ?.let { this.productRepository.getMetricImportance(it) }
    }

    fun addMetricToProduct(productId: Long, metricId: Long): Boolean {
        return this.productRepository.addMetricToProduct(productId, metricId)
    }

    fun removeMetricToProduct(productId: Long, metricId: Long): Boolean {
        return this.productRepository.removeMetricToProduct(productId, metricId)
    }

    fun modifyImportanceOfMetricProduct(productId: Long, metricId: Long, importance: ProductMetricImportance): Boolean {
        return this.productRepository.modifyMetricImportanceOfProduct(productId, metricId, importance)
    }

    @Transactional
    fun changeProductPrice(id: Long, price: Int): Product? {
        return this.getProductById(id)?.changePrice(price)
            ?.also { this.productRepository.save(it) }
    }
}