package com.challenge.geosapiens.service_a.application.controller;

import com.challenge.geosapiens.service_a.domain.usecase.deliveryPerson.DeleteDeliveryPersonUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.deliveryPerson.SaveDeliveryPersonUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.deliveryPerson.UpdateDeliveryPersonUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.DeliveryPersonRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.DeliveryPersonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/delivery-persons")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Delivery Persons", description = "Delivery person management endpoints")
public class DeliveryPersonController {

    private final SaveDeliveryPersonUseCase saveDeliveryPersonUseCase;
    private final UpdateDeliveryPersonUseCase updateDeliveryPersonUseCase;
    private final DeleteDeliveryPersonUseCase deleteDeliveryPersonUseCase;

    @Operation(summary = "Create a new delivery person", description = "Creates a new delivery person with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Delivery person created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "422", description = "Forced exception after 3 tries")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DeliveryPersonResponse> save(@Valid @RequestBody DeliveryPersonRequest deliveryPersonRequest) {
        log.info("[DeliveryPersonController] Received CREATE delivery person request: {}", deliveryPersonRequest);
        DeliveryPersonResponse execute = saveDeliveryPersonUseCase.execute(deliveryPersonRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(execute);
    }

    @Operation(summary = "Update an existing delivery person", description = "Updates a delivery person by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivery person updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Delivery person not found")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DeliveryPersonResponse> update(
            @Valid @RequestBody DeliveryPersonRequest deliveryPersonRequest,
            @Parameter(description = "Delivery Person ID", required = true) @PathVariable UUID id
    ) {
        log.info("[DeliveryPersonController] Received UPDATE delivery person request for id: {}", id);
        DeliveryPersonResponse execute = updateDeliveryPersonUseCase.execute(deliveryPersonRequest, id);
        return ResponseEntity.status(HttpStatus.OK).body(execute);
    }

    @Operation(summary = "Delete a delivery person", description = "Deletes a delivery person by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delivery person deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Delivery person not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@Parameter(description = "Delivery Person ID", required = true) @PathVariable UUID id) {
        log.info("[DeliveryPersonController] Received DELETE delivery person request for id: {}", id);
        deleteDeliveryPersonUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
