package master.kurly.kurlyadmin.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminController {

    @GetMapping("/")
    fun helloWorld(): String{
        return "hello world!"
    }
}