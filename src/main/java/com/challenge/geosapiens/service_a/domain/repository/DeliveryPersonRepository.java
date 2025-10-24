package com.challenge.geosapiens.service_a.domain.repository;

import com.challenge.geosapiens.service_a.domain.entity.DeliveryPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, UUID> {
}
