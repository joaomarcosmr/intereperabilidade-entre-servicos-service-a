package com.challenge.geosapiens.service_a.infrastructure.usecase.user;

import com.challenge.geosapiens.service_a.application.exception.NotFoundException;
import com.challenge.geosapiens.service_a.domain.entity.User;
import com.challenge.geosapiens.service_a.domain.repository.UserRepository;
import com.challenge.geosapiens.service_a.domain.producer.UserSyncProducer;
import com.challenge.geosapiens.service_a.domain.usecase.user.UpdateUserUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.UserRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.UserResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserSyncProducer userSyncProducer;

    @Override
    @Transactional
    public UserResponse execute(UserRequest userRequest, UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());

        User updatedUser = userRepository.save(user);

        userSyncProducer.syncUpdated(updatedUser);

        return userMapper.domainToResponse(updatedUser);
    }

}
