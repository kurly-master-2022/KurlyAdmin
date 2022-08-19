package master.kurly.kurlyadmin.old_controller

import master.kurly.kurlyadmin.repository.ProductDto
import master.kurly.kurlyadmin.service.PriceHistoryDto
import master.kurly.kurlyadmin.service.ProductService
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
    fun getAllProduct(): List<ProductDto> {
        return this.productService.getAllProduct()
    }

    @GetMapping("/name")
    fun getProductByName(
        @RequestParam("value") name: String
    ): ProductDto? {
        return this.productService.getProduct(name)
            .also { this.logger.info("${it?.name}, ${it?.description}, ${it?.price}") }
    }

    @GetMapping("/id")
    fun getProductById(
        @RequestParam("value") id: Long
    ): ProductDto? {
        return this.productService.getProduct(id)
    }

    @GetMapping("/price_history")
    fun getPriceHistory(
        @RequestParam("id") id: Long,
        @RequestParam("datetime") datetime: String?
    ): PriceHistoryDto? {
        return datetime?.let { LocalDateTime.parse(it) }
            ?.let { this.productService.getProductPriceHistory(id, it) }
            ?: this.productService.getProductPriceHistory(id)
    }

    @PostMapping("/change_price")
    fun changeProductPrice(
        @RequestBody changeProductPriceDto: ChangeProductPriceDto
    ){
        this.productService.changeProductPrice(
            changeProductPriceDto.productId,
            changeProductPriceDto.price
        )
    }
}