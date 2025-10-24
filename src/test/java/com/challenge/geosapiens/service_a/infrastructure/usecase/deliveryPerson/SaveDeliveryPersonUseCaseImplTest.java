package com.challenge.geosapiens.service_a.infrastructure.usecase.deliveryPerson;

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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveDeliveryPersonUseCaseImplTest {

    @Mock
    private DeliveryPersonRepository deliveryPersonRepository;

    @Mock
    private DeliveryPersonMapper deliveryPersonMapper;

    @InjectMocks
    private SaveDeliveryPersonUseCaseImpl saveDeliveryPersonUseCase;

    private DeliveryPersonRequest deliveryPersonRequest;
    private DeliveryPerson deliveryPerson;
    private DeliveryPerson savedDeliveryPerson;
    private DeliveryPersonResponse deliveryPersonResponse;

    @BeforeEach
    void setUp() {
        UUID deliveryPersonId = UUID.randomUUID();

        deliveryPersonRequest = new DeliveryPersonRequest();
        deliveryPersonRequest.setName("Carlos Silva");
        deliveryPersonRequest.setPhone("11987654321");

        deliveryPerson = new DeliveryPerson();
        deliveryPerson.setName("Carlos Silva");
        deliveryPerson.setPhone("11987654321");

        savedDeliveryPerson = new DeliveryPerson();
        savedDeliveryPerson.setId(deliveryPersonId);
        savedDeliveryPerson.setName("Carlos Silva");
        savedDeliveryPerson.setPhone("11987654321");

        deliveryPersonResponse = new DeliveryPersonResponse(deliveryPersonId, "Carlos Silva", "11987654321");
    }

    @Test
    void execute_ShouldCreateDeliveryPersonSuccessfully() {
        when(deliveryPersonMapper.toDomain(deliveryPersonRequest)).thenReturn(deliveryPerson);
        when(deliveryPersonRepository.save(any(DeliveryPerson.class))).thenReturn(savedDeliveryPerson);
        when(deliveryPersonMapper.domainToResponse(savedDeliveryPerson)).thenReturn(deliveryPersonResponse);

        DeliveryPersonResponse result = saveDeliveryPersonUseCase.execute(deliveryPersonRequest);

        assertNotNull(result);
        assertEquals(deliveryPersonResponse.getId(), result.getId());
        assertEquals(deliveryPersonResponse.getName(), result.getName());
        assertEquals(deliveryPersonResponse.getPhone(), result.getPhone());
        verify(deliveryPersonRepository, times(1)).save(any(DeliveryPerson.class));
    }
}
