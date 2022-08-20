package master.kurly.kurlyadmin.domain.subscriber

enum class SubscribeType{
    EMAIL, SMS, ALL
}

data class Subscriber(
    val id: Long,
    val name: String,
    val subscribeType: SubscribeType,
    val email: String,
    val phoneNumber: String
)