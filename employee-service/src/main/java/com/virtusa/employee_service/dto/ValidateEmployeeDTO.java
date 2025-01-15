package com.virtusa.employee_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidateEmployeeDTO {

    private boolean valid;
    private Map<String, String> errors;

}
