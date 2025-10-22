package com.challenge.geosapiens.service_a.infrastructure.usecase.user;

import com.challenge.geosapiens.service_a.domain.entity.User;
import com.challenge.geosapiens.service_a.domain.repository.UserRepository;
import com.challenge.geosapiens.service_a.domain.usecase.user.SaveUserUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.UserRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.UserResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveUserUseCaseImpl implements SaveUserUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse execute(UserRequest userRequest) {
        User savedUser = userRepository.save(userMapper.toDomain(userRequest));
        return userMapper.domainToResponse(savedUser);
    }

}