package master.kurly.kurlyadmin.infrastructure.api

import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.metric.ThresholdDirection
import kotlin.math.roundToInt

data class CreateMetricRequestDto(
    val metricId: String,
    val metricName: String,
    val alarmAssessPeriod: Int = 60,
    val alarmEvaluationPeriods: Int = 1,
    val alarmThreshold: Int,
    val alarmComparator: ThresholdDirection,
    val scheduled: Boolean,
    val enabled: Boolean = false,
    val schedCron: String?,
    val metricSourceUri: String?,
){
    companion object {
        fun fromMetric(metric: Metric): CreateMetricRequestDto{

            if (metric.isScheduled){
                if (metric.cronSchedule != null){
                    return CreateMetricRequestDto(
                        metricId = metric.nickname,
                        metricName = metric.name,
                        alarmAssessPeriod = 60,
                        alarmEvaluationPeriods = 1,
                        alarmThreshold = metric.threshold.roundToInt(),
                        alarmComparator = metric.thresholdDirection,
                        scheduled = true,
                        enabled = false,
                        schedCron = metric.cronSchedule,
                        metricSourceUri = metric.source
                    )
                }else {
                    throw IllegalStateException("주기적 실행 메트릭인데 실행 주기가 주어지지 않았습니다.")
                }
            }else{
                return CreateMetricRequestDto(
                    metricId = metric.nickname,
                    metricName = metric.name,
                    alarmAssessPeriod = 60,
                    alarmEvaluationPeriods = 1,
                    alarmThreshold = metric.threshold.roundToInt(),
                    alarmComparator = metric.thresholdDirection,
                    scheduled = false,
                    enabled = false,
                    schedCron = metric.cronSchedule,
                    metricSourceUri = metric.source
                )
            }


        }
    }
}