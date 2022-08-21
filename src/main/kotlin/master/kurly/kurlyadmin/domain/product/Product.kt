package master.kurly.kurlyadmin.domain.product

import java.time.LocalDateTime


enum class ProductMetricImportance {
    VERY_LOW, LOW, MEDIUM, HIGH, VERY_HIGH
}

data class Product(
    val id: Long,
    val name: String,
    val price: Int,
    val description: String,
    val link: String,
    val thumbnail: String,
){

    fun changePrice(
        price: Int
    ): Product{
        return Product(
            id = this.id,
            name = this.name,
            price = price,
            description = this.description,
            link = this.link,
            thumbnail = this.thumbnail
        )
    }
}

data class ProductHistory(
    val product: Product,
    val datetime: List<LocalDateTime>,
    val values: List<Int>
){
    init {
        if (datetime.size != values.size){
            throw IllegalStateException("메트릭 과거값의 형식이 올바르지 않습니다. datetime 과 values 의 배열 크기가 다릅니다.")
        }
    }
}