package com.challenge.geosapiens.service_a.infrastructure.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class DeliveryPersonResponse {
    private UUID id;
    private String name;
    private String phone;
}
