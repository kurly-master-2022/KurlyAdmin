package master.kurly.kurlyadmin.domain.product

import master.kurly.kurlyadmin.domain.metric.Metric


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