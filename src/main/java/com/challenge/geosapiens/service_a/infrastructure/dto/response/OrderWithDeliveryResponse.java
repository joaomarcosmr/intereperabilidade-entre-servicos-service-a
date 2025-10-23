package com.challenge.geosapiens.service_a.infrastructure.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderWithDeliveryResponse {
    private UUID id;
    private String description;
    private Double value;
    private UUID userId;
    private DeliveryPersonResponse deliveryPerson;
    private UserResponse user;
}
