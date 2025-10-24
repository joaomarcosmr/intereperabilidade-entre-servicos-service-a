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
class SaveOrderUseCaseImplTest {

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
    private SaveOrderUseCaseImpl saveOrderUseCase;

    private OrderRequest orderRequest;
    private Order order;
    private Order savedOrder;
    private Order orderWithRelations;
    private User user;
    private DeliveryPerson deliveryPerson;
    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        UUID deliveryPersonId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        orderRequest = new OrderRequest();
        orderRequest.setDescription("Test Order");
        orderRequest.setValue(100.0);
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
        order.setDescription("Test Order");
        order.setValue(100.0);

        savedOrder = new Order();
        savedOrder.setId(orderId);
        savedOrder.setDescription("Test Order");
        savedOrder.setValue(100.0);
        savedOrder.setUser(user);
        savedOrder.setDeliveryPerson(deliveryPerson);

        orderWithRelations = new Order();
        orderWithRelations.setId(orderId);
        orderWithRelations.setDescription("Test Order");
        orderWithRelations.setValue(100.0);
        orderWithRelations.setUser(user);
        orderWithRelations.setDeliveryPerson(deliveryPerson);

        orderResponse = new OrderResponse(orderId, "Test Order", 100.0, userId, deliveryPersonId);
    }

    @Test
    void execute_ShouldCreateOrderSuccessfully() {
        when(orderMapper.toDomain(orderRequest)).thenReturn(order);
        when(deliveryPersonRepository.findById(orderRequest.getDeliveryPersonId())).thenReturn(Optional.of(deliveryPerson));
        when(userRepository.findById(orderRequest.getUserId())).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderRepository.findByIdWithRelations(savedOrder.getId())).thenReturn(Optional.of(orderWithRelations));
        when(orderMapper.domainToResponse(savedOrder)).thenReturn(orderResponse);

        OrderResponse result = saveOrderUseCase.execute(orderRequest);

        assertNotNull(result);
        assertEquals(orderResponse.getId(), result.getId());
        assertEquals(orderResponse.getDescription(), result.getDescription());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderSyncProducer, times(1)).syncCreated(orderWithRelations);
    }

    @Test
    void execute_ShouldThrowNotFoundException_WhenDeliveryPersonNotFound() {
        when(orderMapper.toDomain(orderRequest)).thenReturn(order);
        when(deliveryPersonRepository.findById(orderRequest.getDeliveryPersonId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            saveOrderUseCase.execute(orderRequest);
        });

        assertTrue(exception.getMessage().contains("DeliveryPerson not found with id"));
        verify(orderRepository, never()).save(any(Order.class));
        verify(orderSyncProducer, never()).syncCreated(any(Order.class));
    }

    @Test
    void execute_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(orderMapper.toDomain(orderRequest)).thenReturn(order);
        when(deliveryPersonRepository.findById(orderRequest.getDeliveryPersonId())).thenReturn(Optional.of(deliveryPerson));
        when(userRepository.findById(orderRequest.getUserId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            saveOrderUseCase.execute(orderRequest);
        });

        assertTrue(exception.getMessage().contains("User not found with id"));
        verify(orderRepository, never()).save(any(Order.class));
        verify(orderSyncProducer, never()).syncCreated(any(Order.class));
    }
}
