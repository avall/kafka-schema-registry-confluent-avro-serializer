package sample.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ConfluentAvroConsumerApplication

fun main(args: Array<String>) {
		runApplication<ConfluentAvroConsumerApplication>(*args)
}