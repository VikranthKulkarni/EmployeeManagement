package com.virtusa.employee_service.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EmployeeDTO {

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private boolean employeeStatus;
    private Long salary;
    private Long deptId;
    private DepartmentDTO departmentDTO;

}
