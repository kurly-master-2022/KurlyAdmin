package master.kurly.kurlyadmin.domain.metric

import java.time.LocalDateTime

enum class SourceType{
    API, CSV, JSON, OTHER
}

enum class ThresholdDirection{
    GreaterThanOrEqualToThreshold,
    GreaterThanThreshold,
    LessThanThreshold,
    LessThanOrEqualToThreshold,

    // 실제로 이 값을 사용할 것인지?
    LessThanLowerOrGreaterThanUpperThreshold,
    LessThanLowerThreshold,
    GreaterThanUpperThreshold
}

data class Metric(
    val id: Long,
    val nickname: String,
    val name: String,

    val sourceType: SourceType,
    val source: String,

    val isScheduled: Boolean,
    val cronSchedule: String?,
    val s3ObjectKey: String?,

    val threshold: Double,
    val thresholdDirection: ThresholdDirection,

    val description: String,
    val isAvailable: Boolean = false
){
    init {
        if (this.isScheduled && this.cronSchedule == null){
            throw IllegalArgumentException("주기 실행 작업인데 실행 cron 식이 주어지지 않았습니다.")
        }
    }

    fun activateMetric(): Metric{
        return Metric(
            id = this.id,
            name = this.name,
            nickname = this.nickname,
            source = this.source,
            sourceType = this.sourceType,
            isScheduled = this.isScheduled,
            cronSchedule = this.cronSchedule,
            s3ObjectKey = this.s3ObjectKey,
            threshold = this.threshold,
            thresholdDirection = this.thresholdDirection,
            description = this.description,
            isAvailable = true
        )
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