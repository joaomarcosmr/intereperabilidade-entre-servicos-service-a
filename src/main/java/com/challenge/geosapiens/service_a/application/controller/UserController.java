package com.challenge.geosapiens.service_a.application.controller;

import com.challenge.geosapiens.service_a.domain.usecase.user.DeleteUserUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.user.SaveUserUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.user.UpdateUserUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.UserRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final SaveUserUseCase saveUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<UserResponse> save(@Valid @RequestBody UserRequest userRequest) {
        UserResponse execute = saveUserUseCase.execute(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(execute);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<UserResponse> update(
            @Valid @RequestBody UserRequest userRequest,
            @RequestParam(name = "userId", required = true) UUID id
    ) {
        UserResponse execute = updateUserUseCase.execute(userRequest, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(execute);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<UserResponse> delete(@RequestParam(name = "userId", required = true) UUID id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(203)).body(null);
    }
}
