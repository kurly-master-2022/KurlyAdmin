package master.kurly.kurlyadmin.domain.subscriber

enum class SubscribeType{
    EMAIL, SMS
}

data class Subscriber(
    val id: Long,
    val subscribeType: SubscribeType,
    val uri: String
){
    // name 은 고유키
    val name = "${this.subscribeType.name}.${this.uri}"
}