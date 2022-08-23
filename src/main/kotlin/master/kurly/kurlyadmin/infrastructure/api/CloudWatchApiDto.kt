package master.kurly.kurlyadmin.infrastructure.api

import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.metric.SourceType
import master.kurly.kurlyadmin.domain.metric.ThresholdDirection
import kotlin.math.roundToInt

data class CreateMetricRequestDto(
    val metricId: String,
    val metricName: String?, //default ""
    val metricSourceUri: String,
    val metricType: SourceType,
    val alarmAssessPeriod: Int?, // default 60
    val alarmEvaluationPeriods: Int?, // default 1
    val alarmThreshold: Int,
    val alarmComparator: ThresholdDirection,
    val scheduled: Boolean,
    val schedCron: String?, // scheduled=true이면 필수
    val enabled: Boolean? // default false
){
    companion object {
        fun fromMetric(metric: Metric): CreateMetricRequestDto{

            if (metric.isScheduled){
                if (metric.cronSchedule != null){
                    return CreateMetricRequestDto(
                        metricId = metric.nickname,
                        metricName = metric.name,
                        metricSourceUri = metric.source,
                        metricType = metric.sourceType,
                        alarmAssessPeriod = 60,
                        alarmEvaluationPeriods = 1,
                        alarmThreshold = metric.threshold.roundToInt(),
                        alarmComparator = metric.thresholdDirection,
                        scheduled = true,
                        schedCron = metric.cronSchedule,
                        enabled = false
                    )
                }else {
                    throw IllegalStateException("주기적 실행 메트릭인데 실행 주기가 주어지지 않았습니다.")
                }
            }else{
                return CreateMetricRequestDto(
                    metricId = metric.nickname,
                    metricName = metric.name,
                    metricSourceUri = metric.source,
                    metricType = metric.sourceType,
                    alarmAssessPeriod = 60,
                    alarmEvaluationPeriods = 1,
                    alarmThreshold = metric.threshold.roundToInt(),
                    alarmComparator = metric.thresholdDirection,
                    scheduled = false,
                    schedCron = null,
                    enabled = true
                )
            }


        }
    }
}