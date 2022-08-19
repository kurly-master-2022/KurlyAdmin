package master.kurly.kurlyadmin.service

import master.kurly.kurlyadmin.old_controller.AddMetricDto
import master.kurly.kurlyadmin.old_controller.DeleteMetricDto
import master.kurly.kurlyadmin.old_controller.ModifyMetricImportanceDto
import master.kurly.kurlyadmin.repository.Importance
import master.kurly.kurlyadmin.repository.MetricByProductDto
import master.kurly.kurlyadmin.repository.ProductByMetricDto
import master.kurly.kurlyadmin.repository.ProductMetricRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProductMetricService(
    private val productMetricRepository: ProductMetricRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getMetricsOfProduct(productId: Long): List<MetricByProductDto> {
        return this.productMetricRepository.getMetricsOfProduct(productId)
    }

    fun getProductsOfMetric(metricId: Long): List<ProductByMetricDto> {
        return this.productMetricRepository.getProductsOfMetric(metricId)
    }

    fun addMetricsToProduct(addMetricDto: AddMetricDto): Map<Long, Boolean> {
        return addMetricDto.metricIds.associateWith {
            this.productMetricRepository.addMetricToProduct(addMetricDto.productId, it)
        }
    }

    fun deleteMetricsOfProduct(deleteMetricDto: DeleteMetricDto): Map<Long, Boolean> {
        return deleteMetricDto.metricIds.associateWith {
            this.productMetricRepository.deleteMetricToProduct(deleteMetricDto.productId, it)
        }
    }

    fun modifyMetricImportanceOfProduct(modifyMetricImportanceDto: ModifyMetricImportanceDto): Boolean {
        val importance = when(modifyMetricImportanceDto.importance){
            1 -> Importance.VERY_HIGH
            2 -> Importance.HIGH
            3 -> Importance.MEDIUM
            4 -> Importance.LOW
            5 -> Importance.VERY_LOW
            else -> throw IllegalStateException("중요도가 올바른 값이 아닙니다!")
        }
        return this.productMetricRepository.modifyMetricOfProduct(
            modifyMetricImportanceDto.productId,
            modifyMetricImportanceDto.metricId,
            importance
        )

    }
}