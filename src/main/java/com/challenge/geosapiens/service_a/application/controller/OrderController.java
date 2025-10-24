package com.challenge.geosapiens.service_a.application.controller;

import com.challenge.geosapiens.service_a.domain.usecase.order.DeleteOrderUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.order.SaveOrderUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.order.UpdateOrderUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.OrderRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderResponse;
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
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Orders", description = "Order management endpoints")
public class OrderController {

    private final SaveOrderUseCase saveOrderUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;

    @Operation(summary = "Create a new order", description = "Creates a new order with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User or DeliveryPerson not found"),
            @ApiResponse(responseCode = "422", description = "Forced exception after 3 tries")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderResponse> save(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("[OrderController] Received CREATE order request: {}", orderRequest);
        OrderResponse execute = saveOrderUseCase.execute(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(execute);
    }

    @Operation(summary = "Update an existing order", description = "Updates an order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Order, User or DeliveryPerson not found")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrderResponse> update(
            @Valid @RequestBody OrderRequest orderRequest,
            @Parameter(description = "Order ID", required = true) @PathVariable UUID id
    ) {
        log.info("[OrderController] Received UPDATE order request for id: {}", id);
        OrderResponse execute = updateOrderUseCase.execute(orderRequest, id);
        return ResponseEntity.status(HttpStatus.OK).body(execute);
    }

    @Operation(summary = "Delete an order", description = "Deletes an order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@Parameter(description = "Order ID", required = true) @PathVariable UUID id) {
        log.info("[OrderController] Received DELETE order request for id: {}", id);
        deleteOrderUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
