package master.kurly.kurlyadmin.infrastructure.implementation

import master.kurly.kurlyadmin.domain.metric.Metric
import master.kurly.kurlyadmin.domain.metric.MetricWorkflowManager
import master.kurly.kurlyadmin.infrastructure.api.GithubProjectApi
import master.kurly.kurlyadmin.infrastructure.api.MetricWorkflowApi
import org.springframework.stereotype.Component

@Component
class MetricWorkflowManagerImpl(
    private val metricWorkflowApi: MetricWorkflowApi,
    private val githubProjectApi: GithubProjectApi
): MetricWorkflowManager {

    override fun registerMetricWorkflowJob(metric: Metric): Boolean {
        return this.githubProjectApi.addMetricWorkflowRequest(metric)
    }

    override fun createMetricWorkflow(metric: Metric): Boolean {
        return this.metricWorkflowApi.putMetricRequest(metric)
    }

    override fun activateMetricWorkflow(metric: Metric): Boolean {
        return this.metricWorkflowApi.activateMetricWorkflow(metric)
    }

    override fun deleteMetricWorkflow(metric: Metric): Boolean {
        return this.metricWorkflowApi.deleteMetricWorkflow(metric)
    }
}