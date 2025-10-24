package com.challenge.geosapiens.service_a.application.controller;

import com.challenge.geosapiens.service_a.application.exception.NotFoundException;
import com.challenge.geosapiens.service_a.domain.usecase.user.DeleteUserUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.user.SaveUserUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.user.UpdateUserUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.UserRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private SaveUserUseCase saveUserUseCase;

    @Mock
    private UpdateUserUseCase updateUserUseCase;

    @Mock
    private DeleteUserUseCase deleteUserUseCase;

    @InjectMocks
    private UserController userController;

    private UserRequest userRequest;
    private UserResponse userResponse;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        userRequest = new UserRequest();
        userRequest.setName("João Marcos");
        userRequest.setEmail("joao.marcos@example.com");

        userResponse = new UserResponse(userId, "João Marcos", "joao.marcos@example.com");
    }

    @Test
    void save_ShouldCreateUserSuccessfully() {
        when(saveUserUseCase.execute(any(UserRequest.class))).thenReturn(userResponse);

        ResponseEntity<UserResponse> result = userController.save(userRequest);

        assertNotNull(result);
        assertEquals(201, result.getStatusCode().value());
        assertEquals(userResponse.getId(), result.getBody().getId());
        assertEquals(userResponse.getName(), result.getBody().getName());
        assertEquals(userResponse.getEmail(), result.getBody().getEmail());
        verify(saveUserUseCase, times(1)).execute(any(UserRequest.class));
    }

    @Test
    void update_ShouldUpdateUserSuccessfully() {
        when(updateUserUseCase.execute(any(UserRequest.class), eq(userId))).thenReturn(userResponse);

        ResponseEntity<UserResponse> result = userController.update(userRequest, userId);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(userResponse.getId(), result.getBody().getId());
        verify(updateUserUseCase, times(1)).execute(any(UserRequest.class), eq(userId));
    }

    @Test
    void update_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        when(updateUserUseCase.execute(any(UserRequest.class), eq(userId)))
                .thenThrow(new NotFoundException("User not found"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userController.update(userRequest, userId);
        });

        assertTrue(exception.getMessage().contains("User not found"));
        verify(updateUserUseCase, times(1)).execute(any(UserRequest.class), eq(userId));
    }

    @Test
    void delete_ShouldDeleteUserSuccessfully() {
        doNothing().when(deleteUserUseCase).execute(userId);

        ResponseEntity<Void> result = userController.delete(userId);

        assertNotNull(result);
        assertEquals(203, result.getStatusCode().value());
        verify(deleteUserUseCase, times(1)).execute(userId);
    }

    @Test
    void delete_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        doThrow(new NotFoundException("User not found")).when(deleteUserUseCase).execute(userId);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userController.delete(userId);
        });

        assertTrue(exception.getMessage().contains("User not found"));
        verify(deleteUserUseCase, times(1)).execute(userId);
    }
}