package com.challenge.geosapiens.service_a.infrastructure.mapper;

import com.challenge.geosapiens.service_a.domain.entity.DeliveryPerson;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.DeliveryPersonRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.DeliveryPersonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeliveryPersonMapper {
    DeliveryPerson toDomain(DeliveryPersonRequest deliveryPersonRequest);
    DeliveryPersonResponse domainToResponse(DeliveryPerson deliveryPerson);
}
