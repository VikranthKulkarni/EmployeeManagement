package com.virtusa.manager_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagerDTO {

    private Long managerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private boolean status;
    private Long deptId;
    private Long projectId;


    private DepartmentDTO departmentDTO;
    private ProjectDTO projectDTO;

}
