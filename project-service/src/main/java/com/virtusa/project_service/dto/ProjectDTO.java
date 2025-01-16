package com.virtusa.project_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {

    private Long projectId;
    private String projectName;
    private String projectDescription;
    private Long deptId;
    private Long managerId;

    private DepartmentDTO departmentDTO;
    private ManagerDTO managerDTO;

}
