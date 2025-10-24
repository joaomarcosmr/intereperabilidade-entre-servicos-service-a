package com.challenge.geosapiens.service_a.infrastructure.producer;

import com.challenge.geosapiens.service_a.application.config.RabbitMQConfig;
import com.challenge.geosapiens.service_a.application.util.RequestCounterHelper;
import com.challenge.geosapiens.service_a.domain.entity.DeliveryPerson;
import com.challenge.geosapiens.service_a.domain.entity.Order;
import com.challenge.geosapiens.service_a.domain.entity.User;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderWithDeliveryResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.OrderMapper;
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
class OrderSyncProducerImplTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private RequestCounterHelper requestCounterHelper;

    @InjectMocks
    private OrderSyncProducerImpl orderSyncProducer;

    private Order order;
    private OrderWithDeliveryResponse orderWithDeliveryResponse;

    @BeforeEach
    void setUp() {
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID deliveryPersonId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setName("João Marcos");
        user.setEmail("joao.marcos@example.com");

        DeliveryPerson deliveryPerson = new DeliveryPerson();
        deliveryPerson.setId(deliveryPersonId);
        deliveryPerson.setName("Carlos Silva");
        deliveryPerson.setPhone("11987654321");

        order = new Order();
        order.setId(orderId);
        order.setDescription("Test Order");
        order.setValue(100.0);
        order.setUser(user);
        order.setDeliveryPerson(deliveryPerson);

        orderWithDeliveryResponse = new OrderWithDeliveryResponse(
                orderId, "Test Order", 100.0, userId, "Carlos Silva", "11987654321"
        );
    }

    @Test
    void syncCreated_ShouldPublishMessageSuccessfully() {
        when(requestCounterHelper.shouldFailOrderCreate()).thenReturn(false);
        when(orderMapper.domainToWithDeliveryResponse(order)).thenReturn(orderWithDeliveryResponse);

        orderSyncProducer.syncCreated(order);

        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitMQConfig.ORDER_EXCHANGE),
                eq(RabbitMQConfig.ORDER_CREATE_ROUTING_KEY),
                eq(orderWithDeliveryResponse)
        );
    }

    @Test
    void syncCreated_ShouldThrowException_WhenRequestCounterHelperFails() {
        when(requestCounterHelper.shouldFailOrderCreate()).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> {
            orderSyncProducer.syncCreated(order);
        });

        verify(requestCounterHelper, times(1)).resetCounters();
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    void syncUpdated_ShouldPublishMessageSuccessfully() {
        when(orderMapper.domainToWithDeliveryResponse(order)).thenReturn(orderWithDeliveryResponse);

        orderSyncProducer.syncUpdated(order);

        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitMQConfig.ORDER_EXCHANGE),
                eq(RabbitMQConfig.ORDER_UPDATE_ROUTING_KEY),
                eq(orderWithDeliveryResponse)
        );
    }

    @Test
    void syncDeleted_ShouldPublishMessageSuccessfully() {
        UUID orderId = UUID.randomUUID();

        orderSyncProducer.syncDeleted(orderId);

        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitMQConfig.ORDER_EXCHANGE),
                eq(RabbitMQConfig.ORDER_DELETE_ROUTING_KEY),
                eq(orderId)
        );
    }
}
