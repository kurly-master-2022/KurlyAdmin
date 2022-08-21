package master.kurly.kurlyadmin.domain.subscriber

enum class SubscribeType{
    EMAIL, SMS
}

data class Subscriber(
    val id: Long,
    val subscribeType: SubscribeType,
    val uri: String,
    val name: String
)