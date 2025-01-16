package com.virtusa.project_service.dao;

import com.virtusa.project_service.dto.DepartmentDTO;
import com.virtusa.project_service.dto.EmployeeDTO;
import com.virtusa.project_service.dto.ManagerDTO;
import com.virtusa.project_service.dto.ProjectDTO;
import com.virtusa.project_service.feignClient.DepartmentFeignClient;
import com.virtusa.project_service.feignClient.EmployeeFeignClient;
import com.virtusa.project_service.feignClient.ManagerFeignClient;
import com.virtusa.project_service.model.Project;
import com.virtusa.project_service.repository.ProjectRepo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectDAO {

    private final ProjectRepo projectRepo;
    private final EmployeeFeignClient employeeFeignClient;
    private final ManagerFeignClient managerFeignClient;
    private final DepartmentFeignClient departmentFeignClient;


    public ProjectDAO(ProjectRepo projectRepo, EmployeeFeignClient employeeFeignClient, ManagerFeignClient managerFeignClient, DepartmentFeignClient departmentFeignClient) {
        this.projectRepo = projectRepo;
        this.employeeFeignClient = employeeFeignClient;
        this.managerFeignClient = managerFeignClient;
        this.departmentFeignClient = departmentFeignClient;
    }

    public ProjectDTO saveProject(ProjectDTO projectDTO){
        Project project = toModel(projectDTO);
        project = projectRepo.save(project);

        DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(project.getDeptId());
        ManagerDTO managerDTO = managerFeignClient.getManagerById(project.getManagerId());

        ProjectDTO savedProject = toDTO(project);
        savedProject.setDepartmentDTO(departmentDTO);
        savedProject.setManagerDTO(managerDTO);

        return savedProject;
    }

    public List<ProjectDTO> findAllProjects(){
        List<Project> projectList = projectRepo.findAll();
        return projectList.stream().map(project -> {
            ManagerDTO managerDTO = managerFeignClient.getManagerById(project.getManagerId());
            DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(project.getDeptId());
            ProjectDTO projectDTO = toDTO(project);
            projectDTO.setDepartmentDTO(departmentDTO);
            projectDTO.setManagerDTO(managerDTO);
            return projectDTO;
        }).toList();
    }

    public ProjectDTO findProjectById(Long id){
        Project project = projectRepo.findById(id).orElse(null);
        if (project != null){
            return toDTO(project);
        }else {
            throw new IllegalArgumentException("Project not found by id : " + id);
        }
    }

    public ProjectDTO updateProject(ProjectDTO projectDTO){
        Project existingProject = projectRepo.findById(projectDTO.getProjectId()).orElse(null);
        if (existingProject != null){
           updateProject(existingProject, projectDTO);
           existingProject = projectRepo.save(existingProject);

           ProjectDTO updatedProject = toDTO(existingProject);
            ManagerDTO managerDTO = managerFeignClient.getManagerById(projectDTO.getManagerId());
            DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(projectDTO.getDeptId());
           updatedProject.setManagerDTO(managerDTO);
           updatedProject.setDepartmentDTO(departmentDTO);
            return updatedProject;
        } else {
            throw new IllegalArgumentException("Project not found by id : " + projectDTO.getProjectId());
        }
    }

    public void deleteProjectById(Long id){
        Project project = projectRepo.findById(id).orElse(null);
        if (project != null){
            projectRepo.delete(project);
        } else {
            throw new IllegalArgumentException("Project not found by id : " + id);
        }
    }

    public List<EmployeeDTO> getEmployeesByProjectId(Long projectId){
        return employeeFeignClient.getEmployeesByProjectId(projectId);
    }

    public List<ProjectDTO> getProjectsByDeptId(Long deptId){
        List<Project> projectList = projectRepo.findByDeptId(deptId);
        DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(deptId);
        return projectList.stream().map(project -> {
            ProjectDTO projectDTO = toDTO(project);
            ManagerDTO managerDTO = managerFeignClient.getManagerById(project.getManagerId());
            projectDTO.setManagerDTO(managerDTO);
            projectDTO.setDepartmentDTO(departmentDTO);
            return projectDTO;
        }).toList();
    }


    public ProjectDTO toDTO(Project project){
        return ProjectDTO.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .projectDescription(project.getProjectDescription())
                .deptId(project.getDeptId())
                .managerId(project.getManagerId())
                .build();
    }

    public Project toModel(ProjectDTO projectDTO){
        return Project.builder()
                .projectId(projectDTO.getProjectId())
                .projectName(projectDTO.getProjectName())
                .projectDescription(projectDTO.getProjectDescription())
                .deptId(projectDTO.getDeptId())
                .managerId(projectDTO.getManagerId())
                .build();
    }

    private void updateProject(Project existingProject, ProjectDTO projectDTO){
        existingProject.setProjectName(projectDTO.getProjectName());
        existingProject.setProjectDescription(projectDTO.getProjectDescription());
        existingProject.setDeptId(projectDTO.getDeptId());
        existingProject.setManagerId(projectDTO.getManagerId());
    }

}
