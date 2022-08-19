package master.kurly.kurlyadmin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KurlyAdminApplication

fun main(args: Array<String>) {
    runApplication<KurlyAdminApplication>(*args)
}
