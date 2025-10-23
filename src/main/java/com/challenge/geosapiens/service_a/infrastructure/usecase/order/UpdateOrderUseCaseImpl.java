package com.challenge.geosapiens.service_a.infrastructure.usecase.order;

import com.challenge.geosapiens.service_a.application.exception.NotFoundException;
import com.challenge.geosapiens.service_a.domain.entity.Order;
import com.challenge.geosapiens.service_a.domain.repository.OrderRepository;
import com.challenge.geosapiens.service_a.domain.producer.OrderSyncProducer;
import com.challenge.geosapiens.service_a.domain.usecase.order.UpdateOrderUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.OrderRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateOrderUseCaseImpl implements UpdateOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderSyncProducer orderSyncProducer;

    @Override
    @Transactional
    public OrderResponse execute(OrderRequest orderRequest, UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id));

        order.setDescription(orderRequest.getDescription());
        order.setUserId(orderRequest.getUserId());
        order.setDeliveryPersonId(orderRequest.getDeliveryPersonId());

        Order updatedOrder = orderRepository.save(order);

        orderSyncProducer.syncUpdated(updatedOrder);

        return orderMapper.domainToResponse(updatedOrder);
    }

}
