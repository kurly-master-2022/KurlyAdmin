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
        return this.getProductById(id)?.findMetricAndImportance(this.productRepository)
    }

    @Transactional
    fun changeProductPrice(id: Long, price: Int): Product? {
        return this.getProductById(id)?.changePrice(price, this.productRepository)
    }
}