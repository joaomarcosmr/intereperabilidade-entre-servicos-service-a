package com.challenge.geosapiens.service_a.infrastructure.mapper;

import com.challenge.geosapiens.service_a.domain.entity.Order;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.OrderRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    Order toDomain(OrderRequest orderRequest);
    OrderResponse domainToResponse(Order order);
}
