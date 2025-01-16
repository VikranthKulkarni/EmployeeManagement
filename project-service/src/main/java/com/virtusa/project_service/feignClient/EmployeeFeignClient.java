package com.virtusa.project_service.feignClient;

import com.virtusa.project_service.dto.EmployeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "employee-service")
public interface EmployeeFeignClient {

    @GetMapping("/api/employees/project/{projectId}")
    List<EmployeeDTO> getEmployeesByProjectId(@PathVariable("projectId") Long projectId);

}
