package com.challenge.geosapiens.service_a.domain.usecase.order;

import com.challenge.geosapiens.service_a.infrastructure.dto.request.OrderRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderResponse;

import java.util.UUID;

public interface UpdateOrderUseCase {
    OrderResponse execute(OrderRequest orderRequest, UUID id);
}
