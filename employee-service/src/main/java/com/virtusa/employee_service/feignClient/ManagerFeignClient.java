package com.virtusa.employee_service.feignClient;

import com.virtusa.employee_service.dto.ManagerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "manager-service")
public interface ManagerFeignClient {

    @GetMapping("/api/managers/{id}")
    ManagerDTO getManagerById(@PathVariable("id") Long id);

}
