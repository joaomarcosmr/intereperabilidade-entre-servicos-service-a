package com.challenge.geosapiens.service_a.domain.producer;

import com.challenge.geosapiens.service_a.domain.entity.Order;

public interface OrderSyncProducer {
    void syncCreated(Order order);
    void syncUpdated(Order order);
    void syncDeleted(Long orderId);

}
