package com.challenge.geosapiens.service_a.infrastructure.usecase.deliveryPerson;

import com.challenge.geosapiens.service_a.domain.repository.DeliveryPersonRepository;
import com.challenge.geosapiens.service_a.domain.usecase.deliveryPerson.SaveDeliveryPersonUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveDeliveryPersonUseCaseImpl implements SaveDeliveryPersonUseCase {

    private final DeliveryPersonRepository deliveryPersonRepository;

    @Transactional
    @Override
    public void execute() {

    }

}
