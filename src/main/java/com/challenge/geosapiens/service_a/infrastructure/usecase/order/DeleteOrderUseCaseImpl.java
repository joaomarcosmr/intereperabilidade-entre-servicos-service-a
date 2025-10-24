package com.challenge.geosapiens.service_a.infrastructure.usecase.order;

import com.challenge.geosapiens.service_a.application.exception.NotFoundException;
import com.challenge.geosapiens.service_a.domain.repository.OrderRepository;
import com.challenge.geosapiens.service_a.domain.producer.OrderSyncProducer;
import com.challenge.geosapiens.service_a.domain.usecase.order.DeleteOrderUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteOrderUseCaseImpl implements DeleteOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderSyncProducer orderSyncProducer;

    @Override
    @Transactional
    public void execute(UUID id) {
        log.info("[DeleteOrderUseCase] Starting order deletion for id: {}", id);

        if (!orderRepository.existsById(id)) {
            throw new NotFoundException("Order not found with id: " + id);
        }

        orderRepository.deleteById(id);
        log.info("[DeleteOrderUseCase] Order deleted with id: {}", id);

        orderSyncProducer.syncDeleted(id);
    }

}
