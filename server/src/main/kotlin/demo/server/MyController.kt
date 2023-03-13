package demo.server

import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class MyController(
        val myUserService: MyUserService
) {

    private val log = KotlinLogging.logger {}

    @GetMapping("/user/{userId}")
    fun userName(@PathVariable("userId") userId: String): String {
        log.info("Got a request")
        return myUserService.userName(userId)
    }
}