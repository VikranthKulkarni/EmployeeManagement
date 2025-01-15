package com.virtusa.employee_service.dto;


import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DepartmentDTO {

    private Long deptId;
    private String deptName;

}
