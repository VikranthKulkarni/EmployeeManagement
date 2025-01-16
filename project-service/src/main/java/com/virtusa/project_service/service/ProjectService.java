package com.virtusa.project_service.service;

import com.virtusa.project_service.dao.ProjectDAO;
import com.virtusa.project_service.dto.EmployeeDTO;
import com.virtusa.project_service.dto.ProjectDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectDAO projectDAO;

    public ProjectService(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public ProjectDTO saveProject(ProjectDTO projectDTO){
        return projectDAO.saveProject(projectDTO);
    }

    public List<ProjectDTO> findAllProjects(){
        return projectDAO.findAllProjects();
    }

    public ProjectDTO findProjectById(Long id){
        return projectDAO.findProjectById(id);
    }

    public ProjectDTO updateProject(ProjectDTO projectDTO){
        return projectDAO.updateProject(projectDTO);
    }

    public void deleteProjectById(Long id){
        projectDAO.deleteProjectById(id);
    }

    public List<EmployeeDTO> getEmployeesByProjectId(Long projectId){
        return projectDAO.getEmployeesByProjectId(projectId);
    }

    public List<ProjectDTO> getProjectsByDeptId(Long deptId){
        return projectDAO.getProjectsByDeptId(deptId);
    }

}
