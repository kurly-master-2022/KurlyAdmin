package master.kurly.kurlyadmin.infrastructure.controller

import master.kurly.kurlyadmin.application.ProductService
import master.kurly.kurlyadmin.domain.product.Product
import master.kurly.kurlyadmin.domain.product.ProductHistory
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

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

    @GetMapping("/metric")
    fun getMetricsById(
        @RequestParam("id") id: Long
    ): List<ProductMappingMetricsDto>? {
        return this.productService.getMetricsOfProductById(id)
            ?.map { ProductMappingMetricsDto(it.key, it.value) }
    }

    @PostMapping("/metric")
    fun addMetricToProduct(
        @RequestBody productMetricDto: ProductMetricsDto
    ): Boolean {
        println(productMetricDto)
        return this.productService.addMetricToProduct(
            productMetricDto.productId, productMetricDto.metricIds
        )
    }

    @DeleteMapping("/metric")
    fun deleteMetricToProduct(
        @RequestBody productMetricDto: ProductMetricsDto
    ): Boolean {
        println(productMetricDto)
        return this.productService.removeMetricToProduct(
            productMetricDto.productId, productMetricDto.metricIds
        )
    }

    @PutMapping("/metric")
    fun modifyImportanceOfMetricProduct(
        @RequestBody productMetricImportanceDto: ProductMetricImportanceDto
    ): Boolean {
        return this.productService.modifyImportanceOfMetricProduct(
            productMetricImportanceDto.productId,
            productMetricImportanceDto.metricId,
            productMetricImportanceDto.importance
        )
    }

    @PostMapping("/change_price")
    fun changeProductPrice(
        @RequestBody changeProductPriceDto: ChangeProductPriceDto
    ): Product? {
        return this.productService.changeProductPrice(
            changeProductPriceDto.productId, changeProductPriceDto.price
        )
    }

    @GetMapping("/price_history")
    fun getPriceHistory(
        @RequestParam("id") id: Long,
        @RequestParam("start") startDatetimeString: String?,
        @RequestParam("end") endDatetimeString: String?
    ): ProductHistory? {
        val startDatetime = LocalDateTime.parse(startDatetimeString)
        val endDatetime = LocalDateTime.parse(endDatetimeString)
        return this.productService.getProductHistoryPrice(id, startDatetime, endDatetime)
    }
}