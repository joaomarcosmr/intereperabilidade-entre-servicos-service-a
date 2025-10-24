package com.challenge.geosapiens.service_a.infrastructure.usecase.order;

import com.challenge.geosapiens.service_a.application.exception.NotFoundException;
import com.challenge.geosapiens.service_a.domain.entity.Order;
import com.challenge.geosapiens.service_a.domain.repository.DeliveryPersonRepository;
import com.challenge.geosapiens.service_a.domain.repository.OrderRepository;
import com.challenge.geosapiens.service_a.domain.repository.UserRepository;
import com.challenge.geosapiens.service_a.domain.producer.OrderSyncProducer;
import com.challenge.geosapiens.service_a.domain.usecase.order.SaveOrderUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.OrderRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.OrderMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaveOrderUseCaseImpl implements SaveOrderUseCase {

    private final OrderRepository orderRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final OrderSyncProducer orderSyncProducer;

    @Override
    @Transactional
    public OrderResponse execute(OrderRequest orderRequest) {
        log.info("[SaveOrderUseCase] Starting order creation for userId: {} and deliveryPersonId: {}",
                orderRequest.getUserId(), orderRequest.getDeliveryPersonId());

        Order order = orderMapper.toDomain(orderRequest);

        order.setDeliveryPerson(deliveryPersonRepository.findById(orderRequest.getDeliveryPersonId())
                .orElseThrow(() -> new NotFoundException("DeliveryPerson not found with id: " + orderRequest.getDeliveryPersonId())));

        order.setUser(userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + orderRequest.getUserId())));

        Order savedOrder = orderRepository.save(order);
        log.info("[SaveOrderUseCase] Order saved with id: {}", savedOrder.getId());

        Order orderWithRelations = orderRepository.findByIdWithRelations(savedOrder.getId())
                .orElseThrow(() -> new NotFoundException("Order not found after save: " + savedOrder.getId()));

        orderSyncProducer.syncCreated(orderWithRelations);

        return orderMapper.domainToResponse(savedOrder);
    }

}
