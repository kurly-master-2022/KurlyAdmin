package master.kurly.kurlyadmin

import master.kurly.kurlyadmin.domain.metric.SourceType
import master.kurly.kurlyadmin.domain.metric.ThresholdDirection
import master.kurly.kurlyadmin.domain.subscriber.SubscribeType
import master.kurly.kurlyadmin.infrastructure.entity.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class DataMaker(
    private val productEntityRepository: ProductEntityRepository,
    private val metricEntityRepository: MetricEntityRepository,
    private val subscriberEntityRepository: SubscriberEntityRepository,

    @Value("\${custom-variable.make-initial-data}") private val makeInitial: Boolean
) {

    @PostConstruct
    fun makeInitialData(){
        if (this.makeInitial) this.makeData()
    }

    private fun makeData(){
        val p1 = ProductEntity(null, "아오리 사과 1.5kg(10입내)", 7900, "풋풋한 매력 가득한 제철 사과", "https://www.kurly.com/goods/5056454", "https://img-cf.kurly.com/shop/data/goodsview/20200807/gv10000112080_1.jpg")
        val p2 = ProductEntity(null, "[에이플] GAP 샤인머스켓 포도 500g", 27000, "껍질째 즐기는 짙은 달콤함", "https://www.kurly.com/goods/5054482", "https://img-cf.kurly.com/shop/data/goodsview/20200611/gv00000100239_1.jpg")
        val p3 = ProductEntity(null, "무항생제 1등급 한우 등심덧살 600g", 27500, "100g 당 판매가: 4,583원", "https://www.kurly.com/goods/1000025284", "https://img-cf.kurly.com/shop/data/goodsview/20220816/gv20000408812_1.jpg")
        this.productEntityRepository.saveAll(listOf(p1, p2, p3))

        val m1 = MetricEntity(null, "seoul-daily-rain", "서울특별시 일일 강우량 120mm 탐지", SourceType.API, "http://seoul-daily-rain", true, "0 0 * * * *", null, 120.0, ThresholdDirection.GreaterThanThreshold, "서울시의 일일 강우량 데이터에서 120mm 이상인 경우 알람을 발행합니다", true)
        val m2 = MetricEntity(null, "seoul-temperature", "서울특별시 일일 온도", SourceType.API, "http://seoul-temperature", true, "0 0 * * * *", null, 35.0, ThresholdDirection.GreaterThanThreshold, "서울시의 일일 온도에서 35도 이상을 넘을 경우 알람을 발행합니다.", true)
        val m3 = MetricEntity(null, "test", "테스트 속성입니다.", SourceType.OTHER, "otherSource", false, null, null, 3.0, ThresholdDirection.LessThanThreshold, "테스트 속성입니다.", false)
        this.metricEntityRepository.saveAll(listOf(m1, m2, m3))

        val s1 = SubscriberEntity(null, "${SubscribeType.EMAIL.name}.test@test.com", SubscribeType.EMAIL, "test@test.com")
        val s2 = SubscriberEntity(null, "${SubscribeType.SMS.name}.010-1234-5678", SubscribeType.SMS, "010-1234-5678")
        this.subscriberEntityRepository.saveAll(listOf(s1, s2))
    }
}