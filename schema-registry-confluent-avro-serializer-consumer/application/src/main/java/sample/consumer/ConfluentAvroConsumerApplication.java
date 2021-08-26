package sample.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConfluentAvroConsumerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConfluentAvroConsumerApplication.class, args);
	}
}
