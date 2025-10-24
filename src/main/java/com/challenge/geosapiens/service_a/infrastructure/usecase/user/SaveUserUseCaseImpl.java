package com.challenge.geosapiens.service_a.infrastructure.usecase.user;

import com.challenge.geosapiens.service_a.domain.entity.User;
import com.challenge.geosapiens.service_a.domain.repository.UserRepository;
import com.challenge.geosapiens.service_a.domain.producer.UserSyncProducer;
import com.challenge.geosapiens.service_a.domain.usecase.user.SaveUserUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.UserRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.UserResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaveUserUseCaseImpl implements SaveUserUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserSyncProducer userSyncProducer;

    @Override
    @Transactional
    public UserResponse execute(UserRequest userRequest) {
        log.info("[SaveUserUseCase] Starting user creation for email: {}", userRequest.getEmail());

        User user = userMapper.toDomain(userRequest);

        User savedUser = userRepository.save(user);
        log.info("[SaveUserUseCase] User saved with id: {}", savedUser.getId());

        userSyncProducer.syncCreated(savedUser);

        return userMapper.domainToResponse(savedUser);
    }

}