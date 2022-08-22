package master.kurly.kurlyadmin.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.metric.ThresholdDirection
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers

@Component
class GithubProjectApi(
    @Value("\${custom-variable.github.token}") private val token: String,
    @Value("\${custom-variable.github.project-id}") private val projectId: String
) {
    private val httpClient: HttpClient = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .build()

    fun addMetricWorkflowRequest(metric: Metric): Boolean {
        val threshold = when(metric.thresholdDirection){
            ThresholdDirection.GreaterThanOrEqualToThreshold -> "크거나 같을 때"
            ThresholdDirection.GreaterThanThreshold -> "클 때"
            ThresholdDirection.LessThanThreshold -> "작을 때"
            ThresholdDirection.LessThanOrEqualToThreshold -> "작거나 같을 때"
            else -> "\"에러...\""
        }

        val title = "'${metric.name}' 메트릭 개발 요청"
        val body = "메트릭 **${metric.name}** (별칭 : **${metric.nickname}**) 에 대한 개발을 요청합니다.\\n\\n" +
                "이 메트릭의 타입은 **${metric.sourceType.name}** 이며, 데이터의 원본 소스는 **${metric.source}** 입니다.\\n\\n" +
                "이 메트릭은 **${if (metric.isScheduled) "주기적" else "비주기적"}** 으로 작동${if (metric.isScheduled) "하며, 주기는 `${metric.cronSchedule}` 입니다." else "합니다."}\\n\\n" +
                "메트릭의 수치가 **${metric.threshold} 보다 $threshold** 알람을 발행합니다.\\n\\n" +
                "구체적인 설명은 아래 설명되어 있습니다.\\n\\n<br>" +
                "-> ${metric.description}\\n\\n<br>" +
                "- 개발을 완료한 후엔, **꼭 해당하는 메트릭 페이지에 들어가서 활성화를 눌러주세요.**"
        val postBody = "{\"query\":\"mutation {addProjectV2DraftIssue(input: {projectId: \\\"${this.projectId}\\\" title: \\\"${title}\\\" body: \\\"${body}\\\"}) {projectItem {id}}}\"}"


        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.github.com/graphql"))
            .setHeader("Authorization", "token ${this.token}")
            .POST(BodyPublishers.ofString(postBody))
            .build()

        val response = this.httpClient.send(request, BodyHandlers.ofString())
        return response.statusCode() < 300
    }
}