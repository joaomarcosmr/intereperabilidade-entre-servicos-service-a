package com.challenge.geosapiens.service_a.infrastructure.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderRequest {

    @Schema(hidden = true)
    private UUID id;

    @NotBlank(message = "Descrição é obrigatória")
    private String description;

    @NotNull(message = "Valor do pedido é obrigatório")
    @Positive(message = "O valor não pode ser negativo")
    private Double value;

    @NotNull(message = "ID do usuário é obrigatório")
    private UUID userId;

    @NotNull(message = "ID do entregador é obrigatório")
    private UUID deliveryPersonId;
}
