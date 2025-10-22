package com.challenge.geosapiens.service_a.infrastructure.usecase.user;

import com.challenge.geosapiens.service_a.domain.usecase.user.DeleteUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

    @Override
    public void execute(Long id) {

    }

}
