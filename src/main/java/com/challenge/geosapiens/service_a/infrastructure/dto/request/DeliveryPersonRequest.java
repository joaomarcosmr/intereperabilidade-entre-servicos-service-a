package com.challenge.geosapiens.service_a.infrastructure.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DeliveryPersonRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "^\\d{11}$", message = "Telefone deve conter 11 dígitos no formato brasileiro (ex: 47988888888)")
    private String phone;
}
