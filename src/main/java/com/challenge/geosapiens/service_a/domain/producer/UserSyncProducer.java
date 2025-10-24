package com.challenge.geosapiens.service_a.domain.producer;

import com.challenge.geosapiens.service_a.domain.entity.User;

import java.util.UUID;

public interface UserSyncProducer {
    void syncCreated(User user);
    void syncUpdated(User user);
    void syncDeleted(UUID userId);
}
