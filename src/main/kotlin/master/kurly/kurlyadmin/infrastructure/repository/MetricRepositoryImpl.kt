package master.kurly.kurlyadmin.infrastructure.repository

import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.metric.MetricHistory
import master.kurly.kurlyadmin.domain.metric.MetricRepository
import master.kurly.kurlyadmin.domain.product.Product
import master.kurly.kurlyadmin.domain.subscriber.Subscriber
import master.kurly.kurlyadmin.infrastructure.entity.*
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.client.HttpServerErrorException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDateTime
import javax.transaction.Transactional

@Repository
class MetricRepositoryImpl(
    private val metricEntityRepository: MetricEntityRepository,
    private val subscriberEntityRepository: SubscriberEntityRepository,
    private val productMetricEntityRepository: ProductMetricEntityRepository,
    private val metricSubscriberEntityRepository: MetricSubscriberEntityRepository
): MetricRepository {
    private val httpClient: HttpClient = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .build()
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val req1 = "test"

    private fun postRequest(requestUrl: String, postBody: String): HttpResponse<String> {
        val postRequest = HttpRequest.newBuilder()
            .uri(URI.create(requestUrl))
            .POST(HttpRequest.BodyPublishers.ofString(postBody))
            .build()
        return this.httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString())
    }

    private fun getRequest(requestUrl: String, parameters: Map<String, String>? = null): HttpResponse<String> {
        val parameterString = parameters?.map { "${it.key}=${it.value}" }?.joinToString(separator = "&")?.let { "?${it}" } ?: ""
        val getRequest = HttpRequest.newBuilder()
            .uri(URI.create("${requestUrl}${parameterString}"))
            .build()
        return this.httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString())
    }


    override fun getAllMetrics(): List<Metric> {
        return this.metricEntityRepository.findAll().map { it.toMetric() }
    }

    override fun getMetricById(id: Long): Metric? {
        return this.metricEntityRepository.findById(id).orElse(null)?.toMetric()
    }

    @Transactional
    override fun createMetric(metric: Metric): Boolean {
        this.metricEntityRepository.save(MetricEntity.fromMetric(metric))
        this.postRequest("test", "testBody").let {
            if (it.statusCode() != HttpStatus.OK.value()) {
                throw Error(
                    "메트릭 워크플로우를 만드는 데 실패했습니다!",
                    HttpServerErrorException(HttpStatus.valueOf(it.statusCode()))
                )
            }
        }
        return true
    }

    @Transactional
    override fun deleteMetric(id: Long): Boolean {
        this.metricEntityRepository.findById(id).orElse(null)
            ?.also { metricEntity ->
                this.productMetricEntityRepository.findByMetricId(id)
                    .let { this.productMetricEntityRepository.deleteAll(it) }
                this.metricSubscriberEntityRepository.findByMetricId(id)
                    .let { this.metricSubscriberEntityRepository.deleteAll(it) }
                this.metricEntityRepository.delete(metricEntity)
            }
            ?: run { this.logger.error("메트릭 id 에 해당하는 메트릭이 존재하지 않습니다!") }
        return true
    }

    override fun getProductsOfMetric(metric: Metric): List<Product> {
        return this.metricEntityRepository.findById(metric.id).orElse(null)
            ?.let { metricEntity ->
                this.productMetricEntityRepository.findByMetricId(metricEntity.id!!)
                    .map { it.productEntity!!.toProduct() }
            }
            ?: listOf()
    }

    override fun getSubscribersOfMetric(metric: Metric): List<Subscriber> {
        return this.metricEntityRepository.findById(metric.id).orElse(null)
            ?.let { metricEntity ->
                this.metricSubscriberEntityRepository.findByMetricId(metricEntity.id!!)
                    .map { it.subscriberEntity!!.toSubscriber() }
            }
            ?: listOf()
    }

    @Transactional
    override fun addSubscriberToMetric(metricId: Long, subscriberId: Long): Boolean {
        val metric = this.metricEntityRepository.findById(metricId).orElse(null)
        val subscriber = this.subscriberEntityRepository.findById(subscriberId).orElse(null)

        return if (metric != null && subscriber != null){
            this.metricSubscriberEntityRepository.save(
                MetricSubscriberEntity(null, metric, subscriber)
            )
            TODO("추가 request 보내서 성공하면 하도록 해야함 (transactional)")
            true
        }else{
            this.logger.error("메트릭 id 와 구독자 id 에 해당하는 메트릭과 구독자를 찾지 못했습니다!")
            false
        }
    }

    @Transactional
    override fun removeSubscriberToMetric(metricId: Long, subscriberId: Long): Boolean {

        val metric = this.metricEntityRepository.findById(metricId).orElse(null)
        val subscriber = this.subscriberEntityRepository.findById(subscriberId).orElse(null)

        return if (metric != null && subscriber != null){
            this.metricSubscriberEntityRepository.findByMetricEntityAndSubscriberEntity(metric, subscriber)
                ?.let { this.metricSubscriberEntityRepository.delete(it) }
            TODO("제거 request 보내서 성공하면 하도록 해야함 (transactional)")
            true
        }else{
            this.logger.error("메트릭 id 와 구독자 id 에 해당하는 메트릭과 구독자를 찾지 못했습니다!")
            false
        }
    }

    override fun isMetricAlarmTriggered(metric: Metric): Boolean {
        this.getRequest("isAlarmAvailable", mapOf("metricId" to metric.id.toString()))
        TODO()
    }

    override fun getMetricValueHistory(metric: Metric, startAt: LocalDateTime, endAt: LocalDateTime): MetricHistory? {
        this.getRequest("isAlarmAvailable", mapOf("metricId" to metric.id.toString()))
        TODO()
    }
}