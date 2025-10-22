package com.challenge.geosapiens.service_a.domain.usecase.order;

import com.challenge.geosapiens.service_a.infrastructure.dto.request.OrderRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderResponse;

public interface SaveOrderUseCase {
    OrderResponse execute(OrderRequest orderRequest);
}
