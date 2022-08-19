package master.kurly.kurlyadmin.service

import master.kurly.kurlyadmin.repository.ProductDto
import master.kurly.kurlyadmin.repository.ProductPriceHistoryRepository
import master.kurly.kurlyadmin.repository.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val productPriceHistoryRepository: ProductPriceHistoryRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getAllProduct(): List<ProductDto>{
        return this.productRepository.findAll()
    }

    fun getProduct(name: String): ProductDto? {
        return this.productRepository.findByName(name)
    }

    fun getProduct(id: Long): ProductDto? {
        return this.productRepository.findByIdentifier(id)
    }

    fun changeProductPrice(productId: Long, price: Int){
        this.productRepository.changeProductPrice(productId, price)
    }

    fun getProductPriceHistory(productId: Long): PriceHistoryDto? {
        return this.productPriceHistoryRepository.getPriceHistoryOfProduct(productId)
            ?.fold(PriceHistoryDto(mutableListOf(), mutableListOf())) { default, acc ->
                default.apply {
                    this.datetimes.add(acc.datetime)
                    this.prices.add(acc.price)
                }
            }
    }

    fun getProductPriceHistory(productId: Long, datetime: LocalDateTime): PriceHistoryDto? {
        return this.productPriceHistoryRepository.getPriceHistoryOfProduct(productId, datetime)
            ?.fold(PriceHistoryDto(mutableListOf(), mutableListOf())) { default, acc ->
                default.apply {
                    this.datetimes.add(acc.datetime)
                    this.prices.add(acc.price)
                }
            }
    }
}

data class PriceHistoryDto(
    val datetimes: MutableList<LocalDateTime>,
    val prices: MutableList<Int>
)