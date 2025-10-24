package com.challenge.geosapiens.service_a.infrastructure.usecase.deliveryPerson;

import com.challenge.geosapiens.service_a.application.exception.NotFoundException;
import com.challenge.geosapiens.service_a.domain.entity.DeliveryPerson;
import com.challenge.geosapiens.service_a.domain.repository.DeliveryPersonRepository;
import com.challenge.geosapiens.service_a.domain.usecase.deliveryPerson.UpdateDeliveryPersonUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.DeliveryPersonRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.DeliveryPersonResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.DeliveryPersonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateDeliveryPersonUseCaseImpl implements UpdateDeliveryPersonUseCase {

    private final DeliveryPersonRepository deliveryPersonRepository;
    private final DeliveryPersonMapper deliveryPersonMapper;

    @Override
    @Transactional
    public DeliveryPersonResponse execute(DeliveryPersonRequest deliveryPersonRequest, UUID id) {
        log.info("[UpdateDeliveryPersonUseCase] Starting delivery person update for id: {}", id);

        DeliveryPerson deliveryPerson = deliveryPersonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("DeliveryPerson not found with id: " + id));

        deliveryPersonRequest.setId(deliveryPerson.getId());
        DeliveryPerson updatedDeliveryPerson = deliveryPersonRepository.save(deliveryPersonMapper.toDomain(deliveryPersonRequest));
        log.info("[UpdateDeliveryPersonUseCase] Delivery person updated with id: {}", updatedDeliveryPerson.getId());

        return deliveryPersonMapper.domainToResponse(updatedDeliveryPerson);
    }

}
