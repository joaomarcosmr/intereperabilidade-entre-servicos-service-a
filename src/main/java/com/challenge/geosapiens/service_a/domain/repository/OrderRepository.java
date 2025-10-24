package com.challenge.geosapiens.service_a.domain.repository;

import com.challenge.geosapiens.service_a.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o " +
           "LEFT JOIN FETCH o.deliveryPerson " +
           "WHERE o.id = :id")
    Optional<Order> findByIdWithRelations(@Param("id") UUID id);

}
