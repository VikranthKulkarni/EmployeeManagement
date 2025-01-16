package com.virtusa.manager_service.feignClient;

import com.virtusa.manager_service.dto.ProjectDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "project-service")
public interface ProjectFeignClient {

    @GetMapping("/api/projects/{id}")
    ProjectDTO getProjectById(@PathVariable Long id);

}
