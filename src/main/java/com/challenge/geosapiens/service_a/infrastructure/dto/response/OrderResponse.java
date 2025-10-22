package com.challenge.geosapiens.service_a.infrastructure.dto.response;

import lombok.Data;

@Data
public class OrderResponse {
    private Long id;
    private String description;
    private Integer userId;
    private Integer deliveryPersonId;
}
