package master.kurly.kurlyadmin.infrastructure.api

import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class MetricWorkflowApi {

    private val httpClient: HttpClient = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .build()

    fun postRequest(requestUrl: String, postBody: String): HttpResponse<String> {
        val postRequest = HttpRequest.newBuilder()
            .uri(URI.create(requestUrl))
            .POST(HttpRequest.BodyPublishers.ofString(postBody))
            .build()
        return this.httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString())
    }

    fun getRequest(requestUrl: String, parameters: Map<String, String>? = null): HttpResponse<String> {
        val parameterString = parameters?.map { "${it.key}=${it.value}" }?.joinToString(separator = "&")?.let { "?${it}" } ?: ""
        val getRequest = HttpRequest.newBuilder()
            .uri(URI.create("${requestUrl}${parameterString}"))
            .build()
        return this.httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString())
    }
}