package com.virtusa.project_service.controller;

import com.virtusa.project_service.dto.EmployeeDTO;
import com.virtusa.project_service.dto.ProjectDTO;
import com.virtusa.project_service.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> saveProject(@RequestBody ProjectDTO projectDTO){
        ProjectDTO savedProject = projectService.saveProject(projectDTO);
        return ResponseEntity.ok(savedProject);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects(){
        List<ProjectDTO> projectsList = projectService.findAllProjects();
        return ResponseEntity.ok(projectsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id){
        ProjectDTO projectDTO = projectService.findProjectById(id);
        return ResponseEntity.ok(projectDTO);
    }

    @PutMapping
    public ResponseEntity<ProjectDTO> updateProject(@RequestBody ProjectDTO projectDTO){
        ProjectDTO updatedProject = projectService.updateProject(projectDTO);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectById(@PathVariable Long id){
        projectService.deleteProjectById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employees/{projectId}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByProjectId(@PathVariable Long projectId){
        List<EmployeeDTO> employeeList = projectService.getEmployeesByProjectId(projectId);
        return ResponseEntity.ok(employeeList);
    }

    @GetMapping("/dept/{deptId}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByDeptId(@PathVariable Long deptId){
        List<ProjectDTO> projectsList = projectService.getProjectsByDeptId(deptId);
        return ResponseEntity.ok(projectsList);
    }

}
