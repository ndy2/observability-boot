package demo.client

import io.micrometer.observation.Observation
import io.micrometer.observation.ObservationRegistry
import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate
import kotlin.random.Random


@SpringBootApplication
class ClientApplication {

    private val log = KotlinLogging.logger {}

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate? {
        return builder.build()
    }

    @Bean
    fun myCommandLineRunner(registry: ObservationRegistry, restTemplate: RestTemplate): CommandLineRunner {
        return CommandLineRunner {
            Observation.createNotStarted("my.observation", registry)
                .lowCardinalityKeyValue("userType", listOf("userType1", "userType2", "userType3").random())
                .highCardinalityKeyValue("userId", Random.nextLong(100000).toString())
                .contextualName("command-line-runner")
                .observe {
                    log.info("Will send a request to the server")
                    val response = restTemplate.getForObject(
                        "http://localhost:7654/user/{userId}",
                        String::class.java, Random.nextLong(100000).toString()
                    )
                    log.info("Got response [{}]", response)
                }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<ClientApplication>(*args)
}