package com.virtusa.employee_service.feignClient;

import com.virtusa.employee_service.dto.ProjectDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "project-service")
public interface ProjectFeignClient {

    Logger logger = LoggerFactory.getLogger(ProjectFeignClient.class);

    @GetMapping("/api/projects/{id}")
    @CircuitBreaker(name = "default", fallbackMethod = "fallBackGetProjectById")
    ProjectDTO getProjectById(@PathVariable Long id);

    default ProjectDTO fallBackGetProjectById(Long id, Throwable throwable){
        logger.info("Project fallback called! Reason: {}", throwable.getMessage());

        return ProjectDTO.builder()
                .projectName("Unknown Project")
                .projectDescription("No description available")
                .build();
    }

}
