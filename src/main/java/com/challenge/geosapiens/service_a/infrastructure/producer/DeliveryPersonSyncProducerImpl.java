package com.challenge.geosapiens.service_a.infrastructure.producer;

import com.challenge.geosapiens.service_a.domain.entity.DeliveryPerson;
import com.challenge.geosapiens.service_a.application.exception.ServiceCommunicationException;
import com.challenge.geosapiens.service_a.domain.producer.DeliveryPersonSyncProducer;
import com.challenge.geosapiens.service_a.application.config.RabbitMQConfig;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.DeliveryPersonResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.DeliveryPersonMapper;
import com.challenge.geosapiens.service_a.application.helper.RequestCounterHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryPersonSyncProducerImpl implements DeliveryPersonSyncProducer {

    private final RabbitTemplate rabbitTemplate;
    private final DeliveryPersonMapper deliveryPersonMapper;
    private final RequestCounterHelper requestCounterHelper;

    @Override
    public void syncCreated(DeliveryPerson deliveryPerson) {
        if (requestCounterHelper.shouldFailDeliveryPersonCreate()) {
            log.error("Intentional failure triggered for deliveryPerson {} (CREATE) - Test case 5.1", deliveryPerson.getId());
            requestCounterHelper.resetCounters();
            throw new ServiceCommunicationException(
                    "Exceção intencional após 3 requisições bem-sucedidas - Teste de comunicação com Service B");
        }

        try {
            log.info("Publishing deliveryPerson {} (CREATE) to RabbitMQ", deliveryPerson.getId());
            DeliveryPersonResponse response = deliveryPersonMapper.domainToResponse(deliveryPerson);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.DELIVERY_PERSON_EXCHANGE,
                    RabbitMQConfig.DELIVERY_PERSON_CREATE_ROUTING_KEY,
                    response
            );
            log.info("DeliveryPerson {} successfully published (CREATE) to RabbitMQ", deliveryPerson.getId());
        } catch (Exception e) {
            log.error("Failed to publish deliveryPerson {} (CREATE) to RabbitMQ: {}", deliveryPerson.getId(), e.getMessage());
            throw new ServiceCommunicationException("Falha ao publicar mensagem no RabbitMQ", e);
        }
    }

    @Override
    public void syncUpdated(DeliveryPerson deliveryPerson) {
        try {
            log.info("Publishing deliveryPerson {} (UPDATE) to RabbitMQ", deliveryPerson.getId());
            DeliveryPersonResponse response = deliveryPersonMapper.domainToResponse(deliveryPerson);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.DELIVERY_PERSON_EXCHANGE,
                    RabbitMQConfig.DELIVERY_PERSON_UPDATE_ROUTING_KEY,
                    response
            );
            log.info("DeliveryPerson {} successfully published (UPDATE) to RabbitMQ", deliveryPerson.getId());
        } catch (Exception e) {
            log.error("Failed to publish deliveryPerson {} (UPDATE) to RabbitMQ: {}",
                    deliveryPerson.getId(), e.getMessage());
        }
    }

    @Override
    public void syncDeleted(Long deliveryPersonId) {
        try {
            log.info("Publishing deliveryPerson {} (DELETE) to RabbitMQ", deliveryPersonId);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.DELIVERY_PERSON_EXCHANGE,
                    RabbitMQConfig.DELIVERY_PERSON_DELETE_ROUTING_KEY,
                    deliveryPersonId
            );
            log.info("DeliveryPerson {} successfully published (DELETE) to RabbitMQ", deliveryPersonId);
        } catch (Exception e) {
            log.error("Failed to publish deliveryPerson {} (DELETE) to RabbitMQ: {}",
                    deliveryPersonId, e.getMessage());
        }
    }

}
