package com.challenge.geosapiens.service_a.domain.usecase.user;

import com.challenge.geosapiens.service_a.infrastructure.dto.request.UserRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.UserResponse;

public interface SaveUserUseCase {
    UserResponse execute(UserRequest userRequest);
}
