package com.challenge.geosapiens.service_a.infrastructure.usecase.deliveryPerson;

import com.challenge.geosapiens.service_a.domain.repository.DeliveryPersonRepository;
import com.challenge.geosapiens.service_a.domain.producer.DeliveryPersonSyncProducer;
import com.challenge.geosapiens.service_a.domain.usecase.deliveryPerson.DeleteDeliveryPersonUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteDeliveryPersonUseCaseImpl implements DeleteDeliveryPersonUseCase {

    private final DeliveryPersonRepository deliveryPersonRepository;
    private final DeliveryPersonSyncProducer deliveryPersonSyncProducer;

    @Override
    @Transactional
    public void execute(Long id) {
        if (!deliveryPersonRepository.existsById(id)) {
            throw new RuntimeException("DeliveryPerson not found with id: " + id);
        }

        deliveryPersonRepository.deleteById(id);

        deliveryPersonSyncProducer.syncDeleted(id);
    }

}
