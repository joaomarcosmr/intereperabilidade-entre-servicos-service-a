package com.challenge.geosapiens.service_a.infrastructure.mapper;

import com.challenge.geosapiens.service_a.domain.entity.Order;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.OrderRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderResponse;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderWithDeliveryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {DeliveryPersonMapper.class, UserMapper.class})
public interface OrderMapper {
    Order toDomain(OrderRequest orderRequest);

    @Mapping(source = "deliveryPerson.id", target = "deliveryPersonId")
    @Mapping(source = "user.id", target = "userId")
    OrderResponse domainToResponse(Order order);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "deliveryPerson.name", target = "deliveryPersonName")
    @Mapping(source = "deliveryPerson.phone", target = "deliveryPersonPhone")
    OrderWithDeliveryResponse domainToWithDeliveryResponse(Order order);
}
