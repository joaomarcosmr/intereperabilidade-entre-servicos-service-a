package com.challenge.geosapiens.service_a.infrastructure.usecase.order;

import com.challenge.geosapiens.service_a.application.exception.NotFoundException;
import com.challenge.geosapiens.service_a.domain.producer.OrderSyncProducer;
import com.challenge.geosapiens.service_a.domain.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteOrderUseCaseImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderSyncProducer orderSyncProducer;

    @InjectMocks
    private DeleteOrderUseCaseImpl deleteOrderUseCase;

    @Test
    void execute_ShouldDeleteOrderSuccessfully() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.existsById(orderId)).thenReturn(true);

        deleteOrderUseCase.execute(orderId);

        verify(orderRepository, times(1)).existsById(orderId);
        verify(orderRepository, times(1)).deleteById(orderId);
        verify(orderSyncProducer, times(1)).syncDeleted(orderId);
    }

    @Test
    void execute_ShouldThrowNotFoundException_WhenOrderNotFound() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.existsById(orderId)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            deleteOrderUseCase.execute(orderId);
        });

        assertTrue(exception.getMessage().contains("Order not found"));
        verify(orderRepository, times(1)).existsById(orderId);
        verify(orderRepository, never()).deleteById(orderId);
        verify(orderSyncProducer, never()).syncDeleted(orderId);
    }
}
