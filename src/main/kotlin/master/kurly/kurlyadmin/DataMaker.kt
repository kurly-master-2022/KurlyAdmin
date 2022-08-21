package master.kurly.kurlyadmin

import master.kurly.kurlyadmin.domain.metric.SourceType
import master.kurly.kurlyadmin.domain.metric.ThresholdDirection
import master.kurly.kurlyadmin.domain.subscriber.SubscribeType
import master.kurly.kurlyadmin.infrastructure.entity.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import javax.annotation.PostConstruct

@Component
class DataMaker(
    private val productEntityRepository: ProductEntityRepository,
    private val metricEntityRepository: MetricEntityRepository,
    private val subscriberEntityRepository: SubscriberEntityRepository,
    private val productPriceHistoryEntityRepository: ProductPriceHistoryEntityRepository,

    @Value("\${custom-variable.make-initial-data}") private val makeInitial: Boolean
) {

    @PostConstruct
    fun makeInitialData(){
        if (this.makeInitial) {
            this.makeProductData()
            this.makeMetricData()
            this.makeSubscriberData()
        }
    }

    private fun makeProductData(){
        val p1 = ProductEntity(null, "아오리 사과 1.5kg(10입내)", 7900, "풋풋한 매력 가득한 제철 사과", "https://www.kurly.com/goods/5056454", "https://img-cf.kurly.com/shop/data/goodsview/20200807/gv10000112080_1.jpg")
        val p2 = ProductEntity(null, "[에이플] GAP 샤인머스켓 포도 500g", 27000, "껍질째 즐기는 짙은 달콤함", "https://www.kurly.com/goods/5054482", "https://img-cf.kurly.com/shop/data/goodsview/20200611/gv00000100239_1.jpg")
        val p3 = ProductEntity(null, "무항생제 1등급 한우 등심덧살 600g", 27500, "100g 당 판매가: 4,583원", "https://www.kurly.com/goods/1000025284", "https://img-cf.kurly.com/shop/data/goodsview/20220816/gv20000408812_1.jpg")
        this.productEntityRepository.saveAll(listOf(p1, p2, p3))

        val pp11 = ProductPriceHistoryEntity(null, p1, LocalDateTime.of(2022, 8, 15, 12, 0, 0), 7980)
        val pp12 = ProductPriceHistoryEntity(null, p1, LocalDateTime.of(2022, 8, 16, 13, 4, 0), 7800)
        val pp13 = ProductPriceHistoryEntity(null, p1, LocalDateTime.of(2022, 8, 19, 18, 0, 0), 7760)
        val pp14 = ProductPriceHistoryEntity(null, p1, LocalDateTime.of(2022, 8, 21, 15, 12,0), 7900)
        this.productPriceHistoryEntityRepository.saveAll(listOf(pp11, pp12, pp13, pp14))

        val pp21 = ProductPriceHistoryEntity(null, p2, LocalDateTime.of(2022, 8, 10, 12, 0, 0), 26000)
        val pp22 = ProductPriceHistoryEntity(null, p2, LocalDateTime.of(2022, 8, 12, 13, 4, 0), 26500)
        val pp24 = ProductPriceHistoryEntity(null, p2, LocalDateTime.of(2022, 8, 21, 15, 12,0), 27000)
        this.productPriceHistoryEntityRepository.saveAll(listOf(pp21, pp22, pp24))

        val pp31 = ProductPriceHistoryEntity(null, p3, LocalDateTime.of(2022, 8, 2, 12, 32, 0), 27900)
        val pp32 = ProductPriceHistoryEntity(null, p3, LocalDateTime.of(2022, 8, 6, 13, 4, 0), 29500)
        val pp33 = ProductPriceHistoryEntity(null, p3, LocalDateTime.of(2022, 8, 14, 15, 6, 0), 28500)
        val pp34 = ProductPriceHistoryEntity(null, p3, LocalDateTime.of(2022, 8, 19, 14, 40, 0), 28300)
        val pp35 = ProductPriceHistoryEntity(null, p3, LocalDateTime.of(2022, 8, 21, 15, 12,0), 27500)
        this.productPriceHistoryEntityRepository.saveAll(listOf(pp31, pp32, pp33, pp34, pp35))
    }

    private fun makeMetricData(){
        val m1 = MetricEntity(null, "seoul-daily-rain", "서울특별시 일일 강우량 120mm 탐지", SourceType.API, "http://seoul-daily-rain", true, "0 0 * * * *", null, 120.0, ThresholdDirection.GreaterThanThreshold, "서울시의 일일 강우량 데이터에서 120mm 이상인 경우 알람을 발행합니다", true)
        val m2 = MetricEntity(null, "seoul-temperature", "서울특별시 일일 온도", SourceType.API, "http://seoul-temperature", true, "0 0 * * * *", null, 35.0, ThresholdDirection.GreaterThanThreshold, "서울시의 일일 온도에서 35도 이상을 넘을 경우 알람을 발행합니다.", true)
        val m3 = MetricEntity(null, "test", "테스트 속성입니다.", SourceType.OTHER, "otherSource", false, null, null, 3.0, ThresholdDirection.LessThanThreshold, "테스트 속성입니다.", false)
        this.metricEntityRepository.saveAll(listOf(m1, m2, m3))
    }

    private fun makeSubscriberData(){
        val s1 = SubscriberEntity(null, "${SubscribeType.EMAIL.name}.test@test.com", SubscribeType.EMAIL, "test@test.com")
        val s2 = SubscriberEntity(null, "${SubscribeType.SMS.name}.010-1234-5678", SubscribeType.SMS, "010-1234-5678")
        this.subscriberEntityRepository.saveAll(listOf(s1, s2))
    }

}