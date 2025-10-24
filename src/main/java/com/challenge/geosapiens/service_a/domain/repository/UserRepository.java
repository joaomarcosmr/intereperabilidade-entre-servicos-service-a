package com.challenge.geosapiens.service_a.domain.repository;

import com.challenge.geosapiens.service_a.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
