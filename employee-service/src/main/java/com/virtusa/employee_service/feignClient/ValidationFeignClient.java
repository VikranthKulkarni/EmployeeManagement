package com.virtusa.employee_service.feignClient;


import com.virtusa.employee_service.dto.EmployeeDTO;
import com.virtusa.employee_service.dto.ValidateEmployeeDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "validation-service")
public interface ValidationFeignClient {

    Logger logger = LoggerFactory.getLogger(ValidationFeignClient.class);

    @PostMapping("/api/validate/employee")
    @CircuitBreaker(name = "default", fallbackMethod = "fallBackValidateEmployee")
    ResponseEntity<ValidateEmployeeDTO> validateEmployee(@RequestBody EmployeeDTO employeeDTO);

    default ResponseEntity<ValidateEmployeeDTO> fallBackValidateEmployee(EmployeeDTO employeeDTO, Throwable throwable) {
        logger.error("Validation fallback called! Reason: {}", throwable.getMessage());

        // Create a default response indicating the validation service is unavailable
        ValidateEmployeeDTO fallbackResponse = ValidateEmployeeDTO.builder()
                .valid(false)
                .errors(Map.of(
                        "service", "Validation service is currently unavailable.",
                        "employeeId", employeeDTO.getEmployeeId() != null ? employeeDTO.getEmployeeId().toString() : "Unknown"
                ))
                .build();

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallbackResponse);
    }

}
