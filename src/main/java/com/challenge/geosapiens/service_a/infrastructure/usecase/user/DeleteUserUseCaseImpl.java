package com.challenge.geosapiens.service_a.infrastructure.usecase.user;

import com.challenge.geosapiens.service_a.domain.repository.UserRepository;
import com.challenge.geosapiens.service_a.domain.producer.UserSyncProducer;
import com.challenge.geosapiens.service_a.domain.usecase.user.DeleteUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

    private final UserRepository userRepository;
    private final UserSyncProducer userSyncProducer;

    @Override
    @Transactional
    public void execute(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userRepository.deleteById(id);

        userSyncProducer.syncDeleted(id);
    }

}
