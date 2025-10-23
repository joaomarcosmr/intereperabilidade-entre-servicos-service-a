package com.challenge.geosapiens.service_a.domain.usecase.order;

import java.util.UUID;

public interface DeleteOrderUseCase {
    void execute(UUID id);
}
