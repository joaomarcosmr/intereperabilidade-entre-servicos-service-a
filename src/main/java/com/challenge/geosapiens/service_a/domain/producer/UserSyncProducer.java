package com.challenge.geosapiens.service_a.domain.producer;

import com.challenge.geosapiens.service_a.domain.entity.User;

public interface UserSyncProducer {
    void syncCreated(User user);
    void syncUpdated(User user);
    void syncDeleted(Long userId);
}
