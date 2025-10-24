package com.challenge.geosapiens.service_a.infrastructure.usecase.deliveryPerson;

import com.challenge.geosapiens.service_a.application.exception.NotFoundException;
import com.challenge.geosapiens.service_a.domain.entity.DeliveryPerson;
import com.challenge.geosapiens.service_a.domain.repository.DeliveryPersonRepository;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.DeliveryPersonRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.DeliveryPersonResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.DeliveryPersonMapper;
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
class UpdateDeliveryPersonUseCaseImplTest {

    @Mock
    private DeliveryPersonRepository deliveryPersonRepository;

    @Mock
    private DeliveryPersonMapper deliveryPersonMapper;


    @InjectMocks
    private UpdateDeliveryPersonUseCaseImpl updateDeliveryPersonUseCase;

    private DeliveryPersonRequest deliveryPersonRequest;
    private DeliveryPerson deliveryPerson;
    private DeliveryPerson updatedDeliveryPerson;
    private DeliveryPersonResponse deliveryPersonResponse;
    private UUID deliveryPersonId;

    @BeforeEach
    void setUp() {
        deliveryPersonId = UUID.randomUUID();

        deliveryPersonRequest = new DeliveryPersonRequest();
        deliveryPersonRequest.setName("Carlos Silva Santos");
        deliveryPersonRequest.setPhone("11976543210");

        deliveryPerson = new DeliveryPerson();
        deliveryPerson.setName("Carlos Silva Santos");
        deliveryPerson.setPhone("11976543210");

        updatedDeliveryPerson = new DeliveryPerson();
        updatedDeliveryPerson.setId(deliveryPersonId);
        updatedDeliveryPerson.setName("Carlos Silva Santos");
        updatedDeliveryPerson.setPhone("11976543210");

        deliveryPersonResponse = new DeliveryPersonResponse(deliveryPersonId, "Carlos Silva Santos", "11976543210");
    }

    @Test
    void execute_ShouldUpdateDeliveryPersonSuccessfully() {
        when(deliveryPersonRepository.findById(deliveryPersonId)).thenReturn(Optional.of(updatedDeliveryPerson));
        when(deliveryPersonMapper.toDomain(deliveryPersonRequest)).thenReturn(deliveryPerson);
        when(deliveryPersonRepository.save(any(DeliveryPerson.class))).thenReturn(updatedDeliveryPerson);
        when(deliveryPersonMapper.domainToResponse(updatedDeliveryPerson)).thenReturn(deliveryPersonResponse);

        DeliveryPersonResponse result = updateDeliveryPersonUseCase.execute(deliveryPersonRequest, deliveryPersonId);

        assertNotNull(result);
        assertEquals(deliveryPersonResponse.getId(), result.getId());
        assertEquals(deliveryPersonResponse.getName(), result.getName());
        verify(deliveryPersonRepository, times(1)).save(any(DeliveryPerson.class));
    }

    @Test
    void execute_ShouldThrowNotFoundException_WhenDeliveryPersonNotFound() {
        when(deliveryPersonRepository.findById(deliveryPersonId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            updateDeliveryPersonUseCase.execute(deliveryPersonRequest, deliveryPersonId);
        });

        assertTrue(exception.getMessage().contains("DeliveryPerson not found"));
        verify(deliveryPersonRepository, never()).save(any(DeliveryPerson.class));
    }
}
