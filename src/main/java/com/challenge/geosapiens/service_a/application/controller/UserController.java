package com.challenge.geosapiens.service_a.application.controller;

import com.challenge.geosapiens.service_a.domain.usecase.user.DeleteUserUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.user.SaveUserUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.user.UpdateUserUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.UserRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private final SaveUserUseCase saveUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    @Operation(summary = "Create a new user", description = "Creates a new user with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "422", description = "Forced exception after 3 tries")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> save(@Valid @RequestBody UserRequest userRequest) {
        log.info("[UserController] Received CREATE user request: {}", userRequest);
        UserResponse execute = saveUserUseCase.execute(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(execute);
    }

    @Operation(summary = "Update an existing user", description = "Updates a user by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> update(
            @Valid @RequestBody UserRequest userRequest,
            @Parameter(description = "User ID", required = true) @PathVariable UUID id
    ) {
        log.info("[UserController] Received UPDATE user request for id: {}", id);
        UserResponse execute = updateUserUseCase.execute(userRequest, id);
        return ResponseEntity.status(HttpStatus.OK).body(execute);
    }

    @Operation(summary = "Delete a user", description = "Deletes a user by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "203", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> delete(@Parameter(description = "User ID", required = true) @PathVariable UUID id) {
        log.info("[UserController] Received DELETE user request for id: {}", id);
        deleteUserUseCase.execute(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(203)).body(null);
    }
}
