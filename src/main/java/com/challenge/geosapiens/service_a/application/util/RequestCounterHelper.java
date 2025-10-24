package com.challenge.geosapiens.service_a.application.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class RequestCounterHelper {

    private final AtomicInteger orderCreateCounter = new AtomicInteger(0);
    private final AtomicInteger userCreateCounter = new AtomicInteger(0);

    public boolean shouldFailOrderCreate() {
        int count = orderCreateCounter.incrementAndGet();
        log.info("Order CREATE request count: {}", count);
        return count > 3;
    }

    public boolean shouldFailUserCreate() {
        int count = userCreateCounter.incrementAndGet();
        log.info("User CREATE request count: {}", count);
        return count > 3;
    }

    public void resetCounters() {
        orderCreateCounter.set(0);
        userCreateCounter.set(0);
        log.info("All request counters reset");
    }
}
