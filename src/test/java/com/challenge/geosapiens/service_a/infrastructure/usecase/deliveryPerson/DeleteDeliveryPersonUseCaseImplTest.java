package com.challenge.geosapiens.service_a.infrastructure.usecase.deliveryPerson;

import com.challenge.geosapiens.service_a.application.exception.NotFoundException;
import com.challenge.geosapiens.service_a.domain.repository.DeliveryPersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteDeliveryPersonUseCaseImplTest {

    @Mock
    private DeliveryPersonRepository deliveryPersonRepository;


    @InjectMocks
    private DeleteDeliveryPersonUseCaseImpl deleteDeliveryPersonUseCase;

    @Test
    void execute_ShouldDeleteDeliveryPersonSuccessfully() {
        UUID deliveryPersonId = UUID.randomUUID();
        when(deliveryPersonRepository.existsById(deliveryPersonId)).thenReturn(true);

        deleteDeliveryPersonUseCase.execute(deliveryPersonId);

        verify(deliveryPersonRepository, times(1)).existsById(deliveryPersonId);
        verify(deliveryPersonRepository, times(1)).deleteById(deliveryPersonId);
    }

    @Test
    void execute_ShouldThrowNotFoundException_WhenDeliveryPersonNotFound() {
        UUID deliveryPersonId = UUID.randomUUID();
        when(deliveryPersonRepository.existsById(deliveryPersonId)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            deleteDeliveryPersonUseCase.execute(deliveryPersonId);
        });

        assertTrue(exception.getMessage().contains("DeliveryPerson not found"));
        verify(deliveryPersonRepository, times(1)).existsById(deliveryPersonId);
        verify(deliveryPersonRepository, never()).deleteById(deliveryPersonId);
    }
}
