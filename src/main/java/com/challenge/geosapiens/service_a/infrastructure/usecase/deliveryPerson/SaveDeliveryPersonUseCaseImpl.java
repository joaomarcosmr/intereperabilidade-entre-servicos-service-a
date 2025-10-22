package com.challenge.geosapiens.service_a.infrastructure.usecase.deliveryPerson;

import com.challenge.geosapiens.service_a.domain.entity.DeliveryPerson;
import com.challenge.geosapiens.service_a.domain.repository.DeliveryPersonRepository;
import com.challenge.geosapiens.service_a.domain.producer.DeliveryPersonSyncProducer;
import com.challenge.geosapiens.service_a.domain.usecase.deliveryPerson.SaveDeliveryPersonUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.DeliveryPersonRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.DeliveryPersonResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.DeliveryPersonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaveDeliveryPersonUseCaseImpl implements SaveDeliveryPersonUseCase {

    private final DeliveryPersonRepository deliveryPersonRepository;
    private final DeliveryPersonMapper deliveryPersonMapper;
    private final DeliveryPersonSyncProducer deliveryPersonSyncProducer;

    @Override
    @Transactional
    public DeliveryPersonResponse execute(DeliveryPersonRequest deliveryPersonRequest) {
        DeliveryPerson savedDeliveryPerson = deliveryPersonRepository.save(deliveryPersonMapper.toDomain(deliveryPersonRequest));

        deliveryPersonSyncProducer.syncCreated(savedDeliveryPerson);

        return deliveryPersonMapper.domainToResponse(savedDeliveryPerson);
    }

}
