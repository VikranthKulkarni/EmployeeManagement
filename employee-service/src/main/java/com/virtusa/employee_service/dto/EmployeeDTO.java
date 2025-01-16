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
    private Long managerId;
    private Long projectId;

    //Objects for Dept, Project, Manager - not in model
    private DepartmentDTO departmentDTO;
    private ManagerDTO managerDTO;
    private ProjectDTO projectDTO;

}
