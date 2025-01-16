package com.virtusa.department_service.feignClient;

import com.virtusa.department_service.dto.ProjectDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "project-service")
public interface ProjectFeignClient {

    @GetMapping("/api/projects/dept/{deptId}")
    ResponseEntity<List<ProjectDTO>> getProjectsByDeptId(@PathVariable Long deptId);

}
