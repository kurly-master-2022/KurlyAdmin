package master.kurly.kurlyadmin.infrastructure.controller

import master.kurly.kurlyadmin.application.ProductService
import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.product.Product
import master.kurly.kurlyadmin.domain.product.ProductMetricImportance
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/product")
class ProductController(
    private val productService: ProductService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/all")
    fun getAllProduct(): List<Product> {
        return this.productService.getAllProducts()
    }

    @GetMapping("/id")
    fun getProductById(
        @RequestParam("value") id: Long
    ): Product? {
        return this.productService.getProductById(id)
    }

    @GetMapping("/metrics")
    fun getMetricsById(
        @RequestParam("id") id: Long
    ): Map<Metric, ProductMetricImportance>? {
        return this.productService.getMetricsOfProductById(id)
    }

    @GetMapping("/price_history")
    fun getPriceHistory(
        @RequestParam("id") id: Long,
        @RequestParam("datetime") datetime: String?
    ){
        TODO("구현해야함")
    }

    @PostMapping("/change_price")
    fun changeProductPrice(
        @RequestBody changeProductPriceDto: ChangeProductPriceDto
    ): Product? {
        return this.productService.changeProductPrice(
            changeProductPriceDto.productId, changeProductPriceDto.price
        )
    }


}