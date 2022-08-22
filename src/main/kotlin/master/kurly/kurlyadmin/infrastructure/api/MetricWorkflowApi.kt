package master.kurly.kurlyadmin.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import master.kurly.kurlyadmin.domain.metric.Metric
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class MetricWorkflowApi(
    private val objectMapper: ObjectMapper,
    @Value("\${custom-variable.metric-workflow.url}") private val url: String
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val httpClient: HttpClient = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .build()

    fun putMetricRequest(metric: Metric): Boolean{
        val requestString = CreateMetricRequestDto
            .fromMetric(metric)
            .let { this.objectMapper.writeValueAsString(it) }
        val makeRequest = HttpRequest.newBuilder()
            .uri(URI.create("$url/workflow"))
            .header("content-type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(requestString))
            .build()

        val requestResult = this.httpClient.send(makeRequest, HttpResponse.BodyHandlers.ofString())
        return when(val responseCode = HttpStatus.valueOf(requestResult.statusCode())){
            HttpStatus.OK -> {
                this.logger.info("Metric $metric 의 워크플로 생성이 완료되었습니다!")
                true
            }
            HttpStatus.BAD_REQUEST -> {
                this.logger.error("Metric $metric 의 값이 올바르지 않습니다.")
                false
            }
            HttpStatus.INTERNAL_SERVER_ERROR -> {
                this.logger.error("Metric $metric 생성 과정이 실패했습니다!")
                false
            }
            else -> {
                this.logger.error("test2 : $responseCode")
                this.logger.error("Metric $metric 생성 중 알 수 없는 에러가 발생했습니다!")
                false
            }
        }
    }

    fun deleteMetricWorkflow(metric: Metric): Boolean {
        val deleteRequest = HttpRequest.newBuilder()
            .uri(URI.create("$url/workflow/${metric.nickname}"))
            .build()

        val requestResult = this.httpClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString())
        return when(HttpStatus.valueOf(requestResult.statusCode())){
            HttpStatus.OK -> {
                this.logger.info("Metric $metric 의 워크플로 삭제가 완료되었습니다!")
                true
            }
            HttpStatus.NOT_FOUND -> {
                this.logger.error("Metric $metric 이 워크플로우에 존재하지 않습니다!")
                false
            }
            else -> {
                this.logger.error("Metric $metric 의 삭제 중 알 수 없는 오류가 발생했습니다!")
                false
            }
        }
    }

    fun activateMetricWorkflow(metric: Metric): Boolean {
        val postRequest = HttpRequest.newBuilder()
            .uri(URI.create("$url/workflow/trigger/${metric.nickname}"))
            .build()

        val requestResult = this.httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString())
        return when(HttpStatus.valueOf(requestResult.statusCode())){
            HttpStatus.OK -> {
                this.logger.info("Metric $metric 의 워크플로 활성화가 완료되었습니다!")
                true
            }
            HttpStatus.BAD_REQUEST -> {
                this.logger.info("Metric $metric 은 비동기 워크플로우입니다!")
                false
            }
            HttpStatus.NOT_FOUND -> {
                this.logger.info("없는 메트릭에 대한 활성화를 요청했습니다!")
                false
            }
            else -> {
                this.logger.error("Metric $metric 의 워크플로 활성화 중 알 수 없는 오류가 발생했습니다!")
                false
            }
        }
    }
}