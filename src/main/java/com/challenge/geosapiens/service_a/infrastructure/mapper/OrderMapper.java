package com.challenge.geosapiens.service_a.infrastructure.mapper;

import com.challenge.geosapiens.service_a.domain.entity.Order;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.OrderRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderResponse;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderWithDeliveryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {DeliveryPersonMapper.class, UserMapper.class})
public interface OrderMapper {
    Order toDomain(OrderRequest orderRequest);
    OrderResponse domainToResponse(Order order);
    OrderWithDeliveryResponse domainToWithDeliveryResponse(Order order);
}
