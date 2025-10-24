package com.challenge.geosapiens.service_a.infrastructure.usecase.user;

import com.challenge.geosapiens.service_a.application.exception.NotFoundException;
import com.challenge.geosapiens.service_a.domain.repository.UserRepository;
import com.challenge.geosapiens.service_a.domain.producer.UserSyncProducer;
import com.challenge.geosapiens.service_a.domain.usecase.user.DeleteUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

    private final UserRepository userRepository;
    private final UserSyncProducer userSyncProducer;

    @Override
    @Transactional
    public void execute(UUID id) {
        log.info("[DeleteUserUseCase] Starting user deletion for id: {}", id);

        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
        log.info("[DeleteUserUseCase] User deleted with id: {}", id);

        userSyncProducer.syncDeleted(id);
        log.info("[DeleteUserUseCase] User deletion completed for id: {}", id);
    }

}
