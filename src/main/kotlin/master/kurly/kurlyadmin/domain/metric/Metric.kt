package master.kurly.kurlyadmin.domain.metric

import master.kurly.kurlyadmin.domain.product.Product
import java.time.LocalDateTime

enum class SourceType{
    API, CSV, JSON, OTHER
}

enum class ThresholdDirection{
    UP, DOWN;
}

data class Metric(
    val id: Long,
    val name: String,
    val sourceType: SourceType,
    val source: String,
    val cronSchedule: String?,
    val s3ObjectKey: String?,
    val threshold: Double,
    val thresholdDirection: ThresholdDirection,
    val description: String,
    val isAvailable: Boolean = false
){
    init {
        if((this.cronSchedule == null && this.s3ObjectKey == null) ||
            (this.cronSchedule != null && this.s3ObjectKey != null)) {
            throw IllegalArgumentException("주기 실행 값과 비주기 실행 값이 동시에 주어졌습니다.")
        }
    }
}

data class MetricHistory(
    val metric: Metric,
    val datetime: List<LocalDateTime>,
    val values: List<Double>
){
    init {
        if (datetime.size != values.size){
            throw IllegalStateException("메트릭 과거값의 형식이 올바르지 않습니다. datetime 과 values 의 배열 크기가 다릅니다.")
        }
    }
}