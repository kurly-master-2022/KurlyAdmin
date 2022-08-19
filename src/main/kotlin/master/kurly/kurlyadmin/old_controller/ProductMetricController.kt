package master.kurly.kurlyadmin.old_controller

import master.kurly.kurlyadmin.repository.MetricByProductDto
import master.kurly.kurlyadmin.repository.ProductByMetricDto
import master.kurly.kurlyadmin.service.ProductMetricService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("product_metric")
class ProductMetricController(
    private val productMetricService: ProductMetricService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/by_product")
    fun getMetricsOfProduct(
        @RequestParam("id") id: Long
    ): List<MetricByProductDto> {
        this.logger.info("start!")
        return this.productMetricService.getMetricsOfProduct(id)
            .also { this.logger.info("end"); this.logger.info(it.toString()) }
    }

    @GetMapping("/by_metric")
    fun getProductsOfMetric(
        @RequestParam("id") id: Long
    ): List<ProductByMetricDto> {
        return this.productMetricService.getProductsOfMetric(id)
    }

    @PostMapping("/add_metric")
    fun addMetricsToProduct(
        @RequestBody addMetricDto: AddMetricDto
    ): Map<Long, Boolean> {
        return this.productMetricService.addMetricsToProduct(addMetricDto)
    }

    @PostMapping("/delete_metric")
    fun deleteMetricsToProduct(
        @RequestBody deleteMetricDto: DeleteMetricDto
    ): Map<Long, Boolean> {
        this.logger.info("test : $deleteMetricDto")
        return this.productMetricService.deleteMetricsOfProduct(deleteMetricDto)
    }

    @PostMapping("/modify_metric_importance")
    fun modifyMetricImportance(
        @RequestBody modifyMetricImportanceDto: ModifyMetricImportanceDto
    ): Boolean {
        return this.productMetricService.modifyMetricImportanceOfProduct(modifyMetricImportanceDto)
    }
}