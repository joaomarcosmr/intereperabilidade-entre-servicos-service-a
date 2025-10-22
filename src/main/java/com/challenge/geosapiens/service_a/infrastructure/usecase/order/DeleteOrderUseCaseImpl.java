package com.challenge.geosapiens.service_a.infrastructure.usecase.order;

import com.challenge.geosapiens.service_a.domain.repository.OrderRepository;
import com.challenge.geosapiens.service_a.domain.producer.OrderSyncProducer;
import com.challenge.geosapiens.service_a.domain.usecase.order.DeleteOrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteOrderUseCaseImpl implements DeleteOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderSyncProducer orderSyncProducer;

    @Override
    @Transactional
    public void execute(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }

        orderRepository.deleteById(id);

        orderSyncProducer.syncDeleted(id);
    }

}
