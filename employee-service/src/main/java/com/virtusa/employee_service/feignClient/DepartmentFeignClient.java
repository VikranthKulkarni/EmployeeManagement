package com.virtusa.employee_service.feignClient;

import com.virtusa.employee_service.dto.DepartmentDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "department-service", url = "localhost:8100")
@FeignClient(name = "department-service")
public interface DepartmentFeignClient {

    Logger logger = LoggerFactory.getLogger(DepartmentFeignClient.class);

    @GetMapping("/api/departments/{id}")
    @CircuitBreaker(name = "default", fallbackMethod = "fallbackGetDeptByID")
    DepartmentDTO getDeptByID(@PathVariable("id") Long id);

    default DepartmentDTO fallbackGetDeptByID(Long id, Throwable throwable){

        logger.info("Department Fallback called!, Reason : {}" , throwable.getMessage());
        return DepartmentDTO.builder()
                .deptName("Unknown Department")
                .build();
    }

}
