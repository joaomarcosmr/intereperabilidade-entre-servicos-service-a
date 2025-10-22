package com.challenge.geosapiens.service_a.infrastructure.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderRequest {

    @NotBlank(message = "Descrição é obrigatória")
    private String description;

    @NotNull(message = "Valor do pedido é obrigatório")
    @Positive(message = "O valor não pode ser negativo")
    private Double value;

    @NotNull(message = "ID do usuário é obrigatório")
    @Positive(message = "ID do usuário inválido")
    private Long userId;

    @NotNull(message = "ID do entregador é obrigatório")
    @Positive(message = "ID do usuário inválido")
    private Long deliveryPersonId;
}
