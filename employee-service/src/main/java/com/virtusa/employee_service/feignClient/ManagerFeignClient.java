package com.virtusa.employee_service.feignClient;

import com.virtusa.employee_service.dto.ManagerDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "manager-service")
public interface ManagerFeignClient {

    Logger logger = LoggerFactory.getLogger(ManagerFeignClient.class);

    @GetMapping("/api/managers/{id}")
    @CircuitBreaker(name = "default", fallbackMethod = "fallBackGetManagerById")
    ManagerDTO getManagerById(@PathVariable("id") Long id);

    default ManagerDTO fallBackGetManagerById(Long id, Throwable throwable){
        logger.info("Manager fallback called! Reason: {}", throwable.getMessage() );

        return ManagerDTO.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .email("not.available@example.com")
                .phoneNumber("N/A")
                .build();
    }

}
