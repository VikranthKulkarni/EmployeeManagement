package com.virtusa.manager_service.feignClient;

import com.virtusa.manager_service.dto.DepartmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "department-service")
public interface DepartmentFeignClient {

    @GetMapping("/api/departments/{id}")
    public DepartmentDTO getDeptByID(@PathVariable("id") Long id);

}
