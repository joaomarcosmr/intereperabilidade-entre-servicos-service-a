package com.challenge.geosapiens.service_a.infrastructure.usecase.order;

import com.challenge.geosapiens.service_a.application.exception.NotFoundException;
import com.challenge.geosapiens.service_a.domain.entity.Order;
import com.challenge.geosapiens.service_a.domain.repository.DeliveryPersonRepository;
import com.challenge.geosapiens.service_a.domain.repository.OrderRepository;
import com.challenge.geosapiens.service_a.domain.repository.UserRepository;
import com.challenge.geosapiens.service_a.domain.producer.OrderSyncProducer;
import com.challenge.geosapiens.service_a.domain.usecase.order.UpdateOrderUseCase;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.OrderRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderResponse;
import com.challenge.geosapiens.service_a.infrastructure.mapper.OrderMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateOrderUseCaseImpl implements UpdateOrderUseCase {

    private final OrderRepository orderRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final OrderSyncProducer orderSyncProducer;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public OrderResponse execute(OrderRequest orderRequest, UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id));

        order.setDescription(orderRequest.getDescription());
        order.setValue(orderRequest.getValue());

        // Busca e seta os relacionamentos
        order.setDeliveryPerson(deliveryPersonRepository.findById(orderRequest.getDeliveryPersonId())
                .orElseThrow(() -> new NotFoundException("DeliveryPerson not found with id: " + orderRequest.getDeliveryPersonId())));

        order.setUser(userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + orderRequest.getUserId())));

        Order updatedOrder = orderRepository.save(order);
        log.debug("Updated order with id: {}, deliveryPerson: {}", updatedOrder.getId(), updatedOrder.getDeliveryPerson());

        // Flush para garantir que está no banco
        entityManager.flush();

        // Busca novamente com os relacionamentos
        Order orderWithRelations = orderRepository.findByIdWithRelations(updatedOrder.getId())
                .orElseThrow(() -> new NotFoundException("Order not found after update: " + updatedOrder.getId()));

        log.debug("Order for sync - deliveryPerson: {}", orderWithRelations.getDeliveryPerson());

        orderSyncProducer.syncUpdated(orderWithRelations);

        return orderMapper.domainToResponse(updatedOrder);
    }

}
