package com.virtusa.validation_service.controller;

import com.virtusa.validation_service.dto.EmployeeDTO;
import com.virtusa.validation_service.dto.ValidateEmployeeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/validate")
public class ValidationController {

    @PostMapping("/employee")
    public ResponseEntity<ValidateEmployeeDTO> validateEmployee(@RequestBody EmployeeDTO employeeDTO){
        Map<String, String> errors = new HashMap<>();

        // Validate first name and last name
        if (!employeeDTO.getFirstName().matches("^[A-Za-z]+$")) {
            errors.put("firstName", "First name must contain only letters.");
        }
        if (!employeeDTO.getLastName().matches("^[A-Za-z]+$")) {
            errors.put("lastName", "Last name must contain only letters.");
        }

        // Validate email
        if (!employeeDTO.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errors.put("email", "Invalid email format.");
        }

        // Validate phone number
        if (!employeeDTO.getPhoneNumber().matches("^\\d{10}$")) {
            errors.put("phoneNumber", "Phone number must be 10 digits.");
        }

        // Validate salary
        if (employeeDTO.getSalary() == null || employeeDTO.getSalary() <= 0) {
            errors.put("salary", "Salary must be a positive number.");
        }

        boolean isValid = errors.isEmpty();
        ValidateEmployeeDTO response = ValidateEmployeeDTO.builder()
                .valid(isValid)
                .errors(errors)
                .build();

        return ResponseEntity.ok(response);
    }

}
