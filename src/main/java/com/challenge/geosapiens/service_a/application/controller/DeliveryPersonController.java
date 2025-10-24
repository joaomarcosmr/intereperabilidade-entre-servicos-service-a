package com.challenge.geosapiens.service_a.application.controller;

import com.challenge.geosapiens.service_a.domain.usecase.deliveryPerson.DeleteDeliveryPersonUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.deliveryPerson.SaveDeliveryPersonUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.deliveryPerson.UpdateDeliveryPersonUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.DeliveryPersonRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.DeliveryPersonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/delivery-persons")
@RequiredArgsConstructor
public class DeliveryPersonController {

    private final SaveDeliveryPersonUseCase saveDeliveryPersonUseCase;
    private final UpdateDeliveryPersonUseCase updateDeliveryPersonUseCase;
    private final DeleteDeliveryPersonUseCase deleteDeliveryPersonUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<DeliveryPersonResponse> save(@Valid @RequestBody DeliveryPersonRequest deliveryPersonRequest) {
        DeliveryPersonResponse execute = saveDeliveryPersonUseCase.execute(deliveryPersonRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(execute);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<DeliveryPersonResponse> update(
            @Valid @RequestBody DeliveryPersonRequest deliveryPersonRequest,
            @PathVariable UUID id
    ) {
        DeliveryPersonResponse execute = updateDeliveryPersonUseCase.execute(deliveryPersonRequest, id);
        return ResponseEntity.status(HttpStatus.OK).body(execute);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteDeliveryPersonUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
