package com.challenge.geosapiens.service_a.infrastructure.usecase.user;

import com.challenge.geosapiens.service_a.application.exception.NotFoundException;
import com.challenge.geosapiens.service_a.domain.entity.User;
import com.challenge.geosapiens.service_a.domain.producer.UserSyncProducer;
import com.challenge.geosapiens.service_a.domain.repository.UserRepository;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.UserRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.UserResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserSyncProducer userSyncProducer;

    @InjectMocks
    private UpdateUserUseCaseImpl updateUserUseCase;

    private UserRequest userRequest;
    private User user;
    private User updatedUser;
    private UserResponse userResponse;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        userRequest = new UserRequest();
        userRequest.setName("João Marcos Silva");
        userRequest.setEmail("joao.marcos.silva@example.com");

        user = new User();
        user.setName("João Marcos Silva");
        user.setEmail("joao.marcos.silva@example.com");

        updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName("João Marcos Silva");
        updatedUser.setEmail("joao.marcos.silva@example.com");

        userResponse = new UserResponse(userId, "João Marcos Silva", "joao.marcos.silva@example.com");
    }

    @Test
    void execute_ShouldUpdateUserSuccessfully() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(updatedUser));
        when(userMapper.toDomain(userRequest)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.domainToResponse(updatedUser)).thenReturn(userResponse);

        UserResponse result = updateUserUseCase.execute(userRequest, userId);

        assertNotNull(result);
        assertEquals(userResponse.getId(), result.getId());
        assertEquals(userResponse.getName(), result.getName());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userSyncProducer, times(1)).syncUpdated(updatedUser);
    }

    @Test
    void execute_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            updateUserUseCase.execute(userRequest, userId);
        });

        assertTrue(exception.getMessage().contains("User not found"));
        verify(userRepository, never()).save(any(User.class));
        verify(userSyncProducer, never()).syncUpdated(any(User.class));
    }
}
