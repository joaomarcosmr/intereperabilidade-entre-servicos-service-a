package com.challenge.geosapiens.service_a.infrastructure.producer;

import com.challenge.geosapiens.service_a.domain.entity.Order;
import com.challenge.geosapiens.service_a.application.exception.ServiceCommunicationException;
import com.challenge.geosapiens.service_a.domain.producer.OrderSyncProducer;
import com.challenge.geosapiens.service_a.application.config.RabbitMQConfig;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.OrderMapper;
import com.challenge.geosapiens.service_a.application.helper.RequestCounterHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderSyncProducerImpl implements OrderSyncProducer {

    private final RabbitTemplate rabbitTemplate;
    private final OrderMapper orderMapper;
    private final RequestCounterHelper requestCounterHelper;

    @Override
    public void syncCreated(Order order) {
        if (requestCounterHelper.shouldFailOrderCreate()) {
            log.error("Intentional failure triggered for order {} (CREATE) - Test case 5.1", order.getId());
            requestCounterHelper.resetCounters();
            throw new ServiceCommunicationException(
                    "Exceção intencional após 3 requisições bem-sucedidas - Teste de comunicação com Service B");
        }

        try {
            log.info("Publishing order {} (CREATE) to RabbitMQ", order.getId());
            OrderResponse response = orderMapper.domainToResponse(order);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_CREATE_ROUTING_KEY,
                    response
            );
            log.info("Order {} successfully published (CREATE) to RabbitMQ", order.getId());
        } catch (Exception e) {
            log.error("Failed to publish order {} (CREATE) to RabbitMQ: {}", order.getId(), e.getMessage());
            throw new ServiceCommunicationException("Falha ao publicar mensagem no RabbitMQ", e);
        }
    }

    @Override
    public void syncUpdated(Order order) {
        try {
            log.info("Publishing order {} (UPDATE) to RabbitMQ", order.getId());
            OrderResponse response = orderMapper.domainToResponse(order);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_UPDATE_ROUTING_KEY,
                    response
            );
            log.info("Order {} successfully published (UPDATE) to RabbitMQ", order.getId());
        } catch (Exception e) {
            log.error("Failed to publish order {} (UPDATE) to RabbitMQ: {}", order.getId(), e.getMessage());
        }
    }

    @Override
    public void syncDeleted(Long orderId) {
        try {
            log.info("Publishing order {} (DELETE) to RabbitMQ", orderId);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_DELETE_ROUTING_KEY,
                    orderId
            );
            log.info("Order {} successfully published (DELETE) to RabbitMQ", orderId);
        } catch (Exception e) {
            log.error("Failed to publish order {} (DELETE) to RabbitMQ: {}", orderId, e.getMessage());
        }
    }

}
