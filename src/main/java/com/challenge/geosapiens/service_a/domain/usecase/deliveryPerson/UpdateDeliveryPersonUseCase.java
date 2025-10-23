package com.challenge.geosapiens.service_a.domain.usecase.deliveryPerson;

import com.challenge.geosapiens.service_a.infrastructure.dto.request.DeliveryPersonRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.DeliveryPersonResponse;

import java.util.UUID;

public interface UpdateDeliveryPersonUseCase {
    DeliveryPersonResponse execute(DeliveryPersonRequest deliveryPersonRequest, UUID id);
}
