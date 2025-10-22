package com.challenge.geosapiens.service_a.domain.producer;

import com.challenge.geosapiens.service_a.domain.entity.DeliveryPerson;

public interface DeliveryPersonSyncProducer {
    void syncCreated(DeliveryPerson deliveryPerson);
    void syncUpdated(DeliveryPerson deliveryPerson);
    void syncDeleted(Long deliveryPersonId);
}
