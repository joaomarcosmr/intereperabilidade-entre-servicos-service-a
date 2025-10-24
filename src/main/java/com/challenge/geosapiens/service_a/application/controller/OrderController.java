package com.challenge.geosapiens.service_a.application.controller;

import com.challenge.geosapiens.service_a.domain.usecase.order.DeleteOrderUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.order.SaveOrderUseCase;
import com.challenge.geosapiens.service_a.domain.usecase.order.UpdateOrderUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.OrderRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final SaveOrderUseCase saveOrderUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<OrderResponse> save(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse execute = saveOrderUseCase.execute(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(execute);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<OrderResponse> update(
            @Valid @RequestBody OrderRequest orderRequest,
            @RequestParam(name = "orderId", required = true) UUID id
    ) {
        OrderResponse execute = updateOrderUseCase.execute(orderRequest, id);
        return ResponseEntity.status(HttpStatus.OK).body(execute);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private ResponseEntity<Void> delete(@RequestParam(name = "orderId", required = true) UUID id) {
        deleteOrderUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
