package com.challenge.geosapiens.service_a.infrastructure.producer;

import com.challenge.geosapiens.service_a.domain.entity.User;
import com.challenge.geosapiens.service_a.domain.producer.UserSyncProducer;
import com.challenge.geosapiens.service_a.application.config.RabbitMQConfig;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.UserResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.UserMapper;
import com.challenge.geosapiens.service_a.application.util.RequestCounterHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSyncProducerImpl implements UserSyncProducer {

    private final RabbitTemplate rabbitTemplate;
    private final UserMapper userMapper;
    private final RequestCounterHelper requestCounterHelper;

    @Override
    public void syncCreated(User user) {
        if (requestCounterHelper.shouldFailUserCreate()) {
            log.error("Intentional failure triggered for user {} (CREATE) - Test case 5.1", user.getId());
            requestCounterHelper.resetCounters();
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Exceção intencional");
        }

        try {
            log.info("Publishing user {} (CREATE) to RabbitMQ", user.getId());
            UserResponse response = userMapper.domainToResponse(user);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.USER_EXCHANGE,
                    RabbitMQConfig.USER_CREATE_ROUTING_KEY,
                    response
            );
            log.info("User {} successfully published (CREATE) to RabbitMQ", user.getId());
        } catch (Exception e) {
            log.error("Failed to publish user {} (CREATE) to RabbitMQ: {}", user.getId(), e.getMessage());
        }
    }

    @Override
    public void syncUpdated(User user) {
        try {
            log.info("Publishing user {} (UPDATE) to RabbitMQ", user.getId());
            UserResponse response = userMapper.domainToResponse(user);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.USER_EXCHANGE,
                    RabbitMQConfig.USER_UPDATE_ROUTING_KEY,
                    response
            );
            log.info("User {} successfully published (UPDATE) to RabbitMQ", user.getId());
        } catch (Exception e) {
            log.error("Failed to publish user {} (UPDATE) to RabbitMQ: {}",
                    user.getId(), e.getMessage());
        }
    }

    @Override
    public void syncDeleted(UUID userId) {
        try {
            log.info("Publishing user {} (DELETE) to RabbitMQ", userId);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.USER_EXCHANGE,
                    RabbitMQConfig.USER_DELETE_ROUTING_KEY,
                    userId
            );
            log.info("User {} successfully published (DELETE) to RabbitMQ", userId);
        } catch (Exception e) {
            log.error("Failed to publish user {} (DELETE) to RabbitMQ: {}",
                    userId, e.getMessage());
        }
    }

}
