package com.challenge.geosapiens.service_a.infrastructure.usecase.user;

import com.challenge.geosapiens.service_a.domain.usecase.user.UpdateUserUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.UserRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {

    @Override
    public UserResponse execute(UserRequest userRequest, Long id) {
        return null;
    }

}
