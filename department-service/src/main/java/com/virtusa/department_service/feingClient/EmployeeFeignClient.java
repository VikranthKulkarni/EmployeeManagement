package com.virtusa.department_service.feingClient;

import com.virtusa.department_service.dto.EmployeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "employee-service")
public interface EmployeeFeignClient {

//    @GetMapping("/{id}")
//    EmployeeDTO getEmployeeById(@PathVariable Long id);

    @GetMapping("/byDeptId/{deptId}")
    ResponseEntity<List<EmployeeDTO>> getEmployeesByDeptId(@PathVariable("deptId") Long deptId);
}
