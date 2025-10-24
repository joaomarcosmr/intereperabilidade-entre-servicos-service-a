package com.challenge.geosapiens.service_a.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private UUID id;
    private String description;
    private Double value;
    private UUID userId;
    private UUID deliveryPersonId;
}
