package com.challenge.geosapiens.service_a.application.controller;

import com.challenge.geosapiens.service_a.application.exception.NotFoundException;
import com.challenge.geosapiens.service_a.domain.usecase.deliveryPerson.DeleteDeliveryPersonUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.deliveryPerson.SaveDeliveryPersonUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.deliveryPerson.UpdateDeliveryPersonUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.DeliveryPersonRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.DeliveryPersonResponse;
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
class DeliveryPersonControllerTest {

    @Mock
    private SaveDeliveryPersonUseCase saveDeliveryPersonUseCase;

    @Mock
    private UpdateDeliveryPersonUseCase updateDeliveryPersonUseCase;

    @Mock
    private DeleteDeliveryPersonUseCase deleteDeliveryPersonUseCase;

    @InjectMocks
    private DeliveryPersonController deliveryPersonController;

    private DeliveryPersonRequest deliveryPersonRequest;
    private DeliveryPersonResponse deliveryPersonResponse;
    private UUID deliveryPersonId;

    @BeforeEach
    void setUp() {
        deliveryPersonId = UUID.randomUUID();

        deliveryPersonRequest = new DeliveryPersonRequest();
        deliveryPersonRequest.setName("Carlos Silva");
        deliveryPersonRequest.setPhone("11987654321");

        deliveryPersonResponse = new DeliveryPersonResponse(deliveryPersonId, "Carlos Silva", "11987654321");
    }

    @Test
    void save_ShouldCreateDeliveryPersonSuccessfully() {
        when(saveDeliveryPersonUseCase.execute(any(DeliveryPersonRequest.class))).thenReturn(deliveryPersonResponse);

        ResponseEntity<DeliveryPersonResponse> result = deliveryPersonController.save(deliveryPersonRequest);

        assertNotNull(result);
        assertEquals(201, result.getStatusCode().value());
        assertEquals(deliveryPersonResponse.getId(), result.getBody().getId());
        assertEquals(deliveryPersonResponse.getName(), result.getBody().getName());
        assertEquals(deliveryPersonResponse.getPhone(), result.getBody().getPhone());
        verify(saveDeliveryPersonUseCase, times(1)).execute(any(DeliveryPersonRequest.class));
    }

    @Test
    void update_ShouldUpdateDeliveryPersonSuccessfully() {
        when(updateDeliveryPersonUseCase.execute(any(DeliveryPersonRequest.class), eq(deliveryPersonId)))
                .thenReturn(deliveryPersonResponse);

        ResponseEntity<DeliveryPersonResponse> result = deliveryPersonController.update(deliveryPersonRequest, deliveryPersonId);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(deliveryPersonResponse.getId(), result.getBody().getId());
        verify(updateDeliveryPersonUseCase, times(1)).execute(any(DeliveryPersonRequest.class), eq(deliveryPersonId));
    }

    @Test
    void update_ShouldThrowNotFoundException_WhenDeliveryPersonDoesNotExist() {
        when(updateDeliveryPersonUseCase.execute(any(DeliveryPersonRequest.class), eq(deliveryPersonId)))
                .thenThrow(new NotFoundException("DeliveryPerson not found"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            deliveryPersonController.update(deliveryPersonRequest, deliveryPersonId);
        });

        assertTrue(exception.getMessage().contains("DeliveryPerson not found"));
        verify(updateDeliveryPersonUseCase, times(1)).execute(any(DeliveryPersonRequest.class), eq(deliveryPersonId));
    }

    @Test
    void delete_ShouldDeleteDeliveryPersonSuccessfully() {
        doNothing().when(deleteDeliveryPersonUseCase).execute(deliveryPersonId);

        ResponseEntity<Void> result = deliveryPersonController.delete(deliveryPersonId);

        assertNotNull(result);
        assertEquals(204, result.getStatusCode().value());
        verify(deleteDeliveryPersonUseCase, times(1)).execute(deliveryPersonId);
    }

    @Test
    void delete_ShouldThrowNotFoundException_WhenDeliveryPersonDoesNotExist() {
        doThrow(new NotFoundException("DeliveryPerson not found")).when(deleteDeliveryPersonUseCase).execute(deliveryPersonId);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            deliveryPersonController.delete(deliveryPersonId);
        });

        assertTrue(exception.getMessage().contains("DeliveryPerson not found"));
        verify(deleteDeliveryPersonUseCase, times(1)).execute(deliveryPersonId);
    }
}