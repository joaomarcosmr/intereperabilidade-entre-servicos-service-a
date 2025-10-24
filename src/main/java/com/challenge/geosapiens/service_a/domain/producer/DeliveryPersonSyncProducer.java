package com.challenge.geosapiens.service_a.domain.producer;

import com.challenge.geosapiens.service_a.domain.entity.DeliveryPerson;

import java.util.UUID;

public interface DeliveryPersonSyncProducer {
    void syncCreated(DeliveryPerson deliveryPerson);
    void syncUpdated(DeliveryPerson deliveryPerson);
    void syncDeleted(UUID deliveryPersonId);
}
