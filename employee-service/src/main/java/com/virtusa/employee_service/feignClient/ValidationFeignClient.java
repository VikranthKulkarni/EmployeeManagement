package com.virtusa.employee_service.feignClient;


import com.virtusa.employee_service.dto.EmployeeDTO;
import com.virtusa.employee_service.dto.ValidateEmployeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "validation-service")
public interface ValidationFeignClient {

    @PostMapping("/api/validate/employee")
    ResponseEntity<ValidateEmployeeDTO> validateEmployee(@RequestBody EmployeeDTO employeeDTO);

}
