package master.kurly.kurlyadmin.infrastructure.controller

import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.metric.SourceType
import master.kurly.kurlyadmin.domain.metric.ThresholdDirection
import master.kurly.kurlyadmin.domain.product.Product
import master.kurly.kurlyadmin.domain.product.ProductMetricImportance
import master.kurly.kurlyadmin.domain.subscriber.SubscribeType

data class ChangeProductPriceDto(
    val productId: Long,
    val price: Int
)

data class ProductMetricsDto(
    val productId: Long,
    val metricIds: List<Long>
)

data class ProductMetricImportanceDto(
    val productId: Long,
    val metricId: Long,
    val importanceInteger: Int,
){
    val importance = when(this.importanceInteger){
        1 -> ProductMetricImportance.VERY_HIGH
        2 -> ProductMetricImportance.HIGH
        3 -> ProductMetricImportance.MEDIUM
        4 -> ProductMetricImportance.LOW
        5 -> ProductMetricImportance.VERY_LOW
        else -> throw IllegalArgumentException("잘못된 값이 들어왓습니다.")
    }
}

data class MetricSubscriberDto(
    val metricId: Long,
    val subscriberIds: List<Long>
)

data class MetricCreateDto(
    val nickname: String,
    val name: String,
    val source: String,
    val sourceType: SourceType,
    val alarmThreshold: Double,
    val alarmComparator: ThresholdDirection,
    val scheduled: Boolean,
    val schedCron: String?,
    val description: String
)

data class ProductMappingMetricsDto(
    val metric: Metric,
    val importance: ProductMetricImportance
)


data class MetricMappingProductsDto(
    val product: Product,
    val importance: ProductMetricImportance
)

data class CreateSubscriberDto(
    val name: String,
    val type: SubscribeType,
    val uri: String
)