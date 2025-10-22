package com.challenge.geosapiens.service_a.domain.repository;

import com.challenge.geosapiens.service_a.domain.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
