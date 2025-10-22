package com.challenge.geosapiens.service_a.infrastructure.usecase.order;

import com.challenge.geosapiens.service_a.domain.entity.Order;
import com.challenge.geosapiens.service_a.domain.repository.OrderRepository;
import com.challenge.geosapiens.service_a.domain.producer.OrderSyncProducer;
import com.challenge.geosapiens.service_a.domain.usecase.order.SaveOrderUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.OrderRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaveOrderUseCaseImpl implements SaveOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderSyncProducer orderSyncProducer;

    @Override
    @Transactional
    public OrderResponse execute(OrderRequest orderRequest) {
        Order savedOrder = orderRepository.save(orderMapper.toDomain(orderRequest));

        orderSyncProducer.syncCreated(savedOrder);

        return orderMapper.domainToResponse(savedOrder);
    }

}
