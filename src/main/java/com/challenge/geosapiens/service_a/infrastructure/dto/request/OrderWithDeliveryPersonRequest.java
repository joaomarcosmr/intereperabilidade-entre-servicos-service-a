package com.challenge.geosapiens.service_a.infrastructure.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderWithDeliveryPersonRequest {
    private UUID id;
    private String description;
    private Double value;
    private Integer userId;
    private String deliveryPersonName;
    private String deliveryPersonEmail;
}