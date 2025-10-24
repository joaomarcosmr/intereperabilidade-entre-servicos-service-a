package com.challenge.geosapiens.service_a.application.controller;

import com.challenge.geosapiens.service_a.application.exception.NotFoundException;
import com.challenge.geosapiens.service_a.domain.usecase.order.DeleteOrderUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.order.SaveOrderUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.order.UpdateOrderUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.OrderRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private SaveOrderUseCase saveOrderUseCase;

    @Mock
    private UpdateOrderUseCase updateOrderUseCase;

    @Mock
    private DeleteOrderUseCase deleteOrderUseCase;

    @InjectMocks
    private OrderController orderController;

    private OrderRequest orderRequest;
    private OrderResponse orderResponse;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID deliveryPersonId = UUID.randomUUID();

        orderRequest = new OrderRequest();
        orderRequest.setDescription("Test Order");
        orderRequest.setValue(100.0);
        orderRequest.setUserId(userId);
        orderRequest.setDeliveryPersonId(deliveryPersonId);

        orderResponse = new OrderResponse(orderId, "Test Order", 100.0, userId, deliveryPersonId);
    }

    @Test
    void save_ShouldCreateOrderSuccessfully() {
        when(saveOrderUseCase.execute(any(OrderRequest.class))).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> result = orderController.save(orderRequest);

        assertNotNull(result);
        assertEquals(201, result.getStatusCode().value());
        assertEquals(orderResponse.getId(), result.getBody().getId());
        assertEquals(orderResponse.getDescription(), result.getBody().getDescription());
        assertEquals(orderResponse.getValue(), result.getBody().getValue());
        verify(saveOrderUseCase, times(1)).execute(any(OrderRequest.class));
    }

    @Test
    void update_ShouldUpdateOrderSuccessfully() {
        when(updateOrderUseCase.execute(any(OrderRequest.class), eq(orderId))).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> result = orderController.update(orderRequest, orderId);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(orderResponse.getId(), result.getBody().getId());
        verify(updateOrderUseCase, times(1)).execute(any(OrderRequest.class), eq(orderId));
    }

    @Test
    void update_ShouldThrowNotFoundException_WhenOrderDoesNotExist() {
        when(updateOrderUseCase.execute(any(OrderRequest.class), eq(orderId)))
                .thenThrow(new NotFoundException("Order not found"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            orderController.update(orderRequest, orderId);
        });

        assertTrue(exception.getMessage().contains("Order not found"));
        verify(updateOrderUseCase, times(1)).execute(any(OrderRequest.class), eq(orderId));
    }

    @Test
    void delete_ShouldDeleteOrderSuccessfully() {
        doNothing().when(deleteOrderUseCase).execute(orderId);

        ResponseEntity<Void> result = orderController.delete(orderId);

        assertNotNull(result);
        assertEquals(204, result.getStatusCode().value());
        verify(deleteOrderUseCase, times(1)).execute(orderId);
    }

    @Test
    void delete_ShouldThrowNotFoundException_WhenOrderDoesNotExist() {
        doThrow(new NotFoundException("Order not found")).when(deleteOrderUseCase).execute(orderId);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            orderController.delete(orderId);
        });

        assertTrue(exception.getMessage().contains("Order not found"));
        verify(deleteOrderUseCase, times(1)).execute(orderId);
    }
}