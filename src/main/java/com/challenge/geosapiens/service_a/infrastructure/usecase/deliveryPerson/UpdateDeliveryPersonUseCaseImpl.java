package com.challenge.geosapiens.service_a.infrastructure.usecase.deliveryPerson;

import com.challenge.geosapiens.service_a.domain.entity.DeliveryPerson;
import com.challenge.geosapiens.service_a.domain.repository.DeliveryPersonRepository;
import com.challenge.geosapiens.service_a.domain.producer.DeliveryPersonSyncProducer;
import com.challenge.geosapiens.service_a.domain.usecase.deliveryPerson.UpdateDeliveryPersonUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.DeliveryPersonRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.DeliveryPersonResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.DeliveryPersonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateDeliveryPersonUseCaseImpl implements UpdateDeliveryPersonUseCase {

    private final DeliveryPersonRepository deliveryPersonRepository;
    private final DeliveryPersonMapper deliveryPersonMapper;
    private final DeliveryPersonSyncProducer deliveryPersonSyncProducer;

    @Override
    @Transactional
    public DeliveryPersonResponse execute(DeliveryPersonRequest deliveryPersonRequest, Long id) {
        DeliveryPerson deliveryPerson = deliveryPersonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DeliveryPerson not found with id: " + id));

        deliveryPerson.setName(deliveryPersonRequest.getName());
        deliveryPerson.setEmail(deliveryPersonRequest.getEmail());

        DeliveryPerson updatedDeliveryPerson = deliveryPersonRepository.save(deliveryPerson);

        deliveryPersonSyncProducer.syncUpdated(updatedDeliveryPerson);

        return deliveryPersonMapper.domainToResponse(updatedDeliveryPerson);
    }

}
