package com.challenge.geosapiens.service_a.infrastructure.util;

import com.challenge.geosapiens.service_a.application.config.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RetryHelper {

    private static final String X_DEATH_HEADER = "x-death";
    private static final String COUNT_KEY = "count";

    private final RabbitTemplate rabbitTemplate;

    public RetryHelper(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Verifica se a mensagem excedeu o número máximo de tentativas
     * @param message Mensagem do RabbitMQ
     * @return true se excedeu, false caso contrário
     */
    public boolean hasExceededMaxRetries(Message message) {
        int retryCount = getRetryCount(message);
        return retryCount >= RabbitMQConfig.MAX_RETRY_ATTEMPTS;
    }

    /**
     * Obtém o número de tentativas de reprocessamento da mensagem
     * @param message Mensagem do RabbitMQ
     * @return Número de tentativas
     */
    public int getRetryCount(Message message) {
        MessageProperties properties = message.getMessageProperties();

        if (properties == null) {
            return 0;
        }

        Map<String, Object> headers = properties.getHeaders();

        if (headers == null || !headers.containsKey(X_DEATH_HEADER)) {
            return 0;
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> xDeathHeader = (List<Map<String, Object>>) headers.get(X_DEATH_HEADER);

        if (xDeathHeader == null || xDeathHeader.isEmpty()) {
            return 0;
        }

        // Pega a contagem do primeiro elemento (mais recente)
        Map<String, Object> firstDeath = xDeathHeader.get(0);
        Object count = firstDeath.get(COUNT_KEY);

        if (count instanceof Long) {
            return ((Long) count).intValue();
        } else if (count instanceof Integer) {
            return (Integer) count;
        }

        return 0;
    }

    /**
     * Envia a mensagem para a Dead Letter Queue
     * @param dlxExchange Exchange da DLQ
     * @param routingKey Routing key para a DLQ
     * @param message Mensagem original
     */
    public void sendToDLQ(String dlxExchange, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(dlxExchange, routingKey, message);
    }
}
