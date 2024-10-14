package io.github.robertomessabrasil.kafka_blocking_retry_tutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class NameValidationConsumerApp {

	public static void main(String[] args) {
		SpringApplication.run(NameValidationConsumerApp.class, args);
	}

}
