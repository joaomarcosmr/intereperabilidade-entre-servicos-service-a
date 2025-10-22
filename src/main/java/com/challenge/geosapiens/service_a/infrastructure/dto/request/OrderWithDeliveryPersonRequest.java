package com.challenge.geosapiens.service_a.infrastructure.dto.request;

import lombok.Data;

@Data
public class OrderWithDeliveryPersonRequest {
    private String description;
    private Double value;
    private Integer userId;
    private String deliveryPersonName;
    private String deliveryPersonEmail;
}