package master.kurly.kurlyadmin.infrastructure.controller

import master.kurly.kurlyadmin.domain.product.ProductMetricImportance

data class ChangeProductPriceDto(
    val productId: Long,
    val price: Int
)

data class ProductMetricDto(
    val productId: Long,
    val metricId: Long
)

data class ProductMetricImportanceDto(
    val productId: Long,
    val metricId: Long,
    val importance: ProductMetricImportance
)

data class MetricSubscriberDto(
    val metricId: Long,
    val subscriberId: Long
)