package com.challenge.geosapiens.service_a.domain.rest;

import com.challenge.geosapiens.service_a.infrastructure.dto.response.DeliveryPersonResponse;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.OrderResponse;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "service-b", url = "${service-b.url:http://localhost:8082}")
public interface ServiceCClient {

    // User endpoints
    @PostMapping("/api/users")
    UserResponse createUser(@RequestBody UserResponse userResponse);

    @PutMapping("/api/users/{id}")
    UserResponse updateUser(@PathVariable Long id, @RequestBody UserResponse userResponse);

    @DeleteMapping("/api/users/{id}")
    void deleteUser(@PathVariable Long id);

    // Order endpoints
    @PostMapping("/api/orders")
    OrderResponse createOrder(@RequestBody OrderResponse orderResponse);

    @PutMapping("/api/orders/{id}")
    OrderResponse updateOrder(@PathVariable Long id, @RequestBody OrderResponse orderResponse);

    @DeleteMapping("/api/orders/{id}")
    void deleteOrder(@PathVariable Long id);

    // DeliveryPerson endpoints
    @PostMapping("/api/delivery-persons")
    DeliveryPersonResponse createDeliveryPerson(@RequestBody DeliveryPersonResponse deliveryPersonResponse);

    @PutMapping("/api/delivery-persons/{id}")
    DeliveryPersonResponse updateDeliveryPerson(@PathVariable Long id, @RequestBody DeliveryPersonResponse deliveryPersonResponse);

    @DeleteMapping("/api/delivery-persons/{id}")
    void deleteDeliveryPerson(@PathVariable Long id);

}
