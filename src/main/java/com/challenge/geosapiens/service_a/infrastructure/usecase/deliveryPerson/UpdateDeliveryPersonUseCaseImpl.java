package com.challenge.geosapiens.service_a.infrastructure.usecase.deliveryPerson;

import com.challenge.geosapiens.service_a.application.exception.NotFoundException;
import com.challenge.geosapiens.service_a.domain.entity.DeliveryPerson;
import com.challenge.geosapiens.service_a.domain.repository.DeliveryPersonRepository;
import com.challenge.geosapiens.service_a.domain.usecase.deliveryPerson.UpdateDeliveryPersonUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.DeliveryPersonRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.DeliveryPersonResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.DeliveryPersonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateDeliveryPersonUseCaseImpl implements UpdateDeliveryPersonUseCase {

    private final DeliveryPersonRepository deliveryPersonRepository;
    private final DeliveryPersonMapper deliveryPersonMapper;

    @Override
    @Transactional
    public DeliveryPersonResponse execute(DeliveryPersonRequest deliveryPersonRequest, UUID id) {
        DeliveryPerson deliveryPerson = deliveryPersonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("DeliveryPerson not found with id: " + id));

        deliveryPersonRequest.setId(deliveryPerson.getId());
        DeliveryPerson updatedDeliveryPerson = deliveryPersonRepository.save(deliveryPersonMapper.toDomain(deliveryPersonRequest));
        return deliveryPersonMapper.domainToResponse(updatedDeliveryPerson);
    }

}
