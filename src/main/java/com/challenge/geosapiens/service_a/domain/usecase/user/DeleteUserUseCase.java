package com.challenge.geosapiens.service_a.domain.usecase.user;

import java.util.UUID;

public interface DeleteUserUseCase {
    void execute(UUID id);
}
