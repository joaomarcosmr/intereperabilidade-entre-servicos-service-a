package com.challenge.geosapiens.service_a.infrastructure.usecase.order;

import com.challenge.geosapiens.service_a.application.exception.NotFoundException;
import com.challenge.geosapiens.service_a.domain.entity.DeliveryPerson;
import com.challenge.geosapiens.service_a.domain.entity.Order;
import com.challenge.geosapiens.service_a.domain.entity.User;
import com.challenge.geosapiens.service_a.domain.producer.OrderSyncProducer;
import com.challenge.geosapiens.service_a.domain.repository.DeliveryPersonRepository;
import com.challenge.geosapiens.service_a.domain.repository.OrderRepository;
import com.challenge.geosapiens.service_a.domain.repository.UserRepository;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.OrderRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateOrderUseCaseImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DeliveryPersonRepository deliveryPersonRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderSyncProducer orderSyncProducer;

    @InjectMocks
    private UpdateOrderUseCaseImpl updateOrderUseCase;

    private OrderRequest orderRequest;
    private Order order;
    private Order updatedOrder;
    private User user;
    private DeliveryPerson deliveryPerson;
    private OrderResponse orderResponse;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        UUID deliveryPersonId = UUID.randomUUID();
        orderId = UUID.randomUUID();

        orderRequest = new OrderRequest();
        orderRequest.setDescription("Updated Order");
        orderRequest.setValue(150.0);
        orderRequest.setUserId(userId);
        orderRequest.setDeliveryPersonId(deliveryPersonId);

        user = new User();
        user.setId(userId);
        user.setName("João Marcos");
        user.setEmail("joao.marcos@example.com");

        deliveryPerson = new DeliveryPerson();
        deliveryPerson.setId(deliveryPersonId);
        deliveryPerson.setName("Carlos Silva");
        deliveryPerson.setPhone("11987654321");

        order = new Order();
        order.setDescription("Updated Order");
        order.setValue(150.0);

        updatedOrder = new Order();
        updatedOrder.setId(orderId);
        updatedOrder.setDescription("Updated Order");
        updatedOrder.setValue(150.0);
        updatedOrder.setUser(user);
        updatedOrder.setDeliveryPerson(deliveryPerson);

        orderResponse = new OrderResponse(orderId, "Updated Order", 150.0, userId, deliveryPersonId);
    }

    @Test
    void execute_ShouldUpdateOrderSuccessfully() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(updatedOrder));
        when(orderMapper.toDomain(orderRequest)).thenReturn(order);
        when(deliveryPersonRepository.findById(orderRequest.getDeliveryPersonId())).thenReturn(Optional.of(deliveryPerson));
        when(userRepository.findById(orderRequest.getUserId())).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);
        when(orderRepository.findByIdWithRelations(orderId)).thenReturn(Optional.of(updatedOrder));
        when(orderMapper.domainToResponse(updatedOrder)).thenReturn(orderResponse);

        OrderResponse result = updateOrderUseCase.execute(orderRequest, orderId);

        assertNotNull(result);
        assertEquals(orderResponse.getId(), result.getId());
        assertEquals(orderResponse.getDescription(), result.getDescription());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderSyncProducer, times(1)).syncUpdated(updatedOrder);
    }

    @Test
    void execute_ShouldThrowNotFoundException_WhenOrderNotFound() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            updateOrderUseCase.execute(orderRequest, orderId);
        });

        assertTrue(exception.getMessage().contains("Order not found with id"));
        verify(orderRepository, never()).save(any(Order.class));
        verify(orderSyncProducer, never()).syncUpdated(any(Order.class));
    }

    @Test
    void execute_ShouldThrowNotFoundException_WhenDeliveryPersonNotFound() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(updatedOrder));
        when(deliveryPersonRepository.findById(orderRequest.getDeliveryPersonId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            updateOrderUseCase.execute(orderRequest, orderId);
        });

        assertTrue(exception.getMessage().contains("DeliveryPerson not found with id"));
        verify(orderRepository, never()).save(any(Order.class));
        verify(orderSyncProducer, never()).syncUpdated(any(Order.class));
    }
}
