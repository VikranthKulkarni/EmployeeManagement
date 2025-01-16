package com.virtusa.employee_service.feignClient;

import com.virtusa.employee_service.dto.DepartmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "department-service", url = "localhost:8100")
@FeignClient(name = "department-service")
public interface DepartmentFeignClient {

    @GetMapping("/api/departments/{id}")
    DepartmentDTO getDeptByID(@PathVariable("id") Long id);

}
