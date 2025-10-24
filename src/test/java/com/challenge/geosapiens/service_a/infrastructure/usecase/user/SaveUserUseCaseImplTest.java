package com.challenge.geosapiens.service_a.infrastructure.usecase.user;

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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserSyncProducer userSyncProducer;

    @InjectMocks
    private SaveUserUseCaseImpl saveUserUseCase;

    private UserRequest userRequest;
    private User user;
    private User savedUser;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();

        userRequest = new UserRequest();
        userRequest.setName("João Marcos");
        userRequest.setEmail("joao.marcos@example.com");

        user = new User();
        user.setName("João Marcos");
        user.setEmail("joao.marcos@example.com");

        savedUser = new User();
        savedUser.setId(userId);
        savedUser.setName("João Marcos");
        savedUser.setEmail("joao.marcos@example.com");

        userResponse = new UserResponse(userId, "João Marcos", "joao.marcos@example.com");
    }

    @Test
    void execute_ShouldCreateUserSuccessfully() {
        when(userMapper.toDomain(userRequest)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.domainToResponse(savedUser)).thenReturn(userResponse);

        UserResponse result = saveUserUseCase.execute(userRequest);

        assertNotNull(result);
        assertEquals(userResponse.getId(), result.getId());
        assertEquals(userResponse.getName(), result.getName());
        assertEquals(userResponse.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userSyncProducer, times(1)).syncCreated(savedUser);
    }
}
