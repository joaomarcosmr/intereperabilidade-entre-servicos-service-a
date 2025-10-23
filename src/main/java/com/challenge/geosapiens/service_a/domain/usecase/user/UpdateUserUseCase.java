package com.challenge.geosapiens.service_a.domain.usecase.user;

import com.challenge.geosapiens.service_a.infrastructure.dto.request.UserRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.UserResponse;

import java.util.UUID;

public interface UpdateUserUseCase {
    UserResponse execute(UserRequest userRequest, UUID id);
}
