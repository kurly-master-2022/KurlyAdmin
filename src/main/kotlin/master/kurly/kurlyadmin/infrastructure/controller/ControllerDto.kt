package master.kurly.kurlyadmin.infrastructure.controller

data class ChangeProductPriceDto(
    val productId: Long,
    val price: Int
)

data class AddMetricDto(
    val productId: Long,
    val metricIds: List<Long>
)

data class DeleteMetricDto(
    val productId: Long,
    val metricIds: List<Long>
)

data class ModifyMetricImportanceDto(
    val productId: Long,
    val metricId: Long,
    val importance: Int
)

data class NewMetricDto(
    val name: String,
    val sourceType: String,
    val isPeriodic: Boolean,
    val schedule: String?,
    val description: String?
)