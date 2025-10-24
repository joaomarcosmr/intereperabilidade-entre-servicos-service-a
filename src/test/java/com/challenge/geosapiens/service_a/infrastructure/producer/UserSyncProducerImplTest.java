package com.challenge.geosapiens.service_a.infrastructure.producer;

import com.challenge.geosapiens.service_a.application.config.RabbitMQConfig;
import com.challenge.geosapiens.service_a.application.util.RequestCounterHelper;
import com.challenge.geosapiens.service_a.domain.entity.User;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.UserResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSyncProducerImplTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RequestCounterHelper requestCounterHelper;

    @InjectMocks
    private UserSyncProducerImpl userSyncProducer;

    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setName("João Marcos");
        user.setEmail("joao.marcos@example.com");

        userResponse = new UserResponse(userId, "João Marcos", "joao.marcos@example.com");
    }

    @Test
    void syncCreated_ShouldPublishMessageSuccessfully() {
        when(requestCounterHelper.shouldFailUserCreate()).thenReturn(false);
        when(userMapper.domainToResponse(user)).thenReturn(userResponse);

        userSyncProducer.syncCreated(user);

        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitMQConfig.USER_EXCHANGE),
                eq(RabbitMQConfig.USER_CREATE_ROUTING_KEY),
                eq(userResponse)
        );
    }

    @Test
    void syncCreated_ShouldThrowException_WhenRequestCounterHelperFails() {
        when(requestCounterHelper.shouldFailUserCreate()).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> {
            userSyncProducer.syncCreated(user);
        });

        verify(requestCounterHelper, times(1)).resetCounters();
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    void syncUpdated_ShouldPublishMessageSuccessfully() {
        when(userMapper.domainToResponse(user)).thenReturn(userResponse);

        userSyncProducer.syncUpdated(user);

        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitMQConfig.USER_EXCHANGE),
                eq(RabbitMQConfig.USER_UPDATE_ROUTING_KEY),
                eq(userResponse)
        );
    }

    @Test
    void syncDeleted_ShouldPublishMessageSuccessfully() {
        UUID userId = UUID.randomUUID();

        userSyncProducer.syncDeleted(userId);

        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitMQConfig.USER_EXCHANGE),
                eq(RabbitMQConfig.USER_DELETE_ROUTING_KEY),
                eq(userId)
        );
    }
}
