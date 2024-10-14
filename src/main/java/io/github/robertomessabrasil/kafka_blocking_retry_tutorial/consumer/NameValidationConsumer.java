package io.github.robertomessabrasil.kafka_blocking_retry_tutorial.consumer;

import io.github.robertomessabrasil.kafka_blocking_retry_tutorial.exception.ApiCallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Component;

@Component
public class NameValidationConsumer {
    private static Logger LOGGER = LoggerFactory.getLogger(NameValidationConsumer.class);

    @Value("${topics.name-validation-dlt}")
    private String validationNameDTL;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(
            topics = "${topics.name-validation}",
            groupId = "consumer-group-0")
    @Retryable(retryFor = ApiCallException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    void consume(String message) throws ApiCallException {
        LOGGER.info("Retry count:{}", getRetryCount());
        callApi();
    }

    private void callApi() throws ApiCallException {
        throw new ApiCallException();
    }

    @Recover
    void recover(ApiCallException ex, String message) {
        LOGGER.info("Sending to DLT...");
        kafkaTemplate.send(validationNameDTL, message);
    }

    private static int getRetryCount() {
        return RetrySynchronizationManager
                .getContext()
                .getRetryCount();
    }
}
