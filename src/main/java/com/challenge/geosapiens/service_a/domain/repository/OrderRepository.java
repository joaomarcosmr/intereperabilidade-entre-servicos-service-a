package com.challenge.geosapiens.service_a.domain.repository;

import com.challenge.geosapiens.service_a.domain.entity.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, UUID> {
}
