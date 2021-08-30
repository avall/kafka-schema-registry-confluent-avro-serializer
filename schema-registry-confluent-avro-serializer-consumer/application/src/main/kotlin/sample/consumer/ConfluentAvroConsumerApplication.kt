package sample.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = ["com.sample", "sample.consumer"])
class ConfluentAvroConsumerApplication

fun main(args: Array<String>) {
		runApplication<ConfluentAvroConsumerApplication>(*args)
}