package com.virtusa.manager_service.dao;

import com.virtusa.manager_service.dto.DepartmentDTO;
import com.virtusa.manager_service.dto.EmployeeDTO;
import com.virtusa.manager_service.dto.ManagerDTO;
import com.virtusa.manager_service.dto.ProjectDTO;
import com.virtusa.manager_service.feignClient.DepartmentFeignClient;
import com.virtusa.manager_service.feignClient.EmployeeFeignClient;
import com.virtusa.manager_service.feignClient.ProjectFeignClient;
import com.virtusa.manager_service.model.Manager;
import com.virtusa.manager_service.repository.ManagerRepo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManagerDAO {

    private final ManagerRepo managerRepo;
    private final EmployeeFeignClient employeeFeignClient;
    private final ProjectFeignClient projectFeignClient;
    private final DepartmentFeignClient departmentFeignClient;


    public ManagerDAO(ManagerRepo managerRepo, EmployeeFeignClient employeeFeignClient, ProjectFeignClient projectFeignClient, DepartmentFeignClient departmentFeignClient) {
        this.managerRepo = managerRepo;
        this.employeeFeignClient = employeeFeignClient;
        this.projectFeignClient = projectFeignClient;
        this.departmentFeignClient = departmentFeignClient;
    }

    public ManagerDTO saveManager(ManagerDTO managerDTO){
        Manager manager = toModel(managerDTO);
        manager = managerRepo.save(manager);

        DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(manager.getDeptId());
        ProjectDTO projectDTO = projectFeignClient.getProjectById(manager.getProjectId());

        ManagerDTO savedManager = toDTO(manager);
        savedManager.setDepartmentDTO(departmentDTO);
        savedManager.setProjectDTO(projectDTO);

        return savedManager;
    }

    public ManagerDTO findManagerById(Long id){
        Manager manager = managerRepo.findById(id).orElse(null);
        if(manager != null) {
            return toDTO(manager);
        } else {
            throw new IllegalArgumentException("Manager not found by id : " + id);
        }
    }

    public List<ManagerDTO> findAllManagers(){
        List<Manager> managers = managerRepo.findAll();
        return managers.stream().map(manager -> {
            DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(manager.getDeptId());
            ProjectDTO projectDTO = projectFeignClient.getProjectById(manager.getProjectId());
            ManagerDTO managerDTO = toDTO(manager);
            managerDTO.setDepartmentDTO(departmentDTO);
            managerDTO.setProjectDTO(projectDTO);
            return managerDTO;
        }).toList();
    }

    public ManagerDTO updateManager(ManagerDTO managerDTO){
        Manager exsitingManager = managerRepo.findById(managerDTO.getManagerId()).orElse(null);
        if(exsitingManager != null){
            updateManagerDetails(exsitingManager,managerDTO);
            exsitingManager = managerRepo.save(exsitingManager);
            ManagerDTO updatedManager = toDTO(exsitingManager);
            DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(managerDTO.getDeptId());
            ProjectDTO projectDTO = projectFeignClient.getProjectById(managerDTO.getProjectId());
            updatedManager.setProjectDTO(projectDTO);
            updatedManager.setDepartmentDTO(departmentDTO);
            return updatedManager;
        } else {
            throw new IllegalArgumentException("Manager not found by id : " + managerDTO.getManagerId());
        }
    }

    public void deleteManagerById(Long id){
        Manager manager = managerRepo.findById(id).orElse(null);
        if(manager != null){
            manager.setStatus(false);
            managerRepo.save(manager);
        } else {
            throw new IllegalArgumentException("Manager not found by id : " + id);
        }
    }

    public List<ManagerDTO> getManagersByDeptId(Long deptId){
        List<Manager> managerList = managerRepo.findByDeptId(deptId);
        DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(deptId);

        return managerList.stream().map(manager -> {
            ManagerDTO managerDTO = toDTO(manager);
            ProjectDTO projectDTO = projectFeignClient.getProjectById(managerDTO.getProjectId());
            managerDTO.setDepartmentDTO(departmentDTO);
            managerDTO.setProjectDTO(projectDTO);
            return managerDTO;
        }).toList();
    }

    public List<EmployeeDTO> getEmployeesByManagerId(Long managerId){
        return employeeFeignClient.getEmployeesByManagerId(managerId).getBody();
    }

    public ManagerDTO toDTO(Manager manager){
        return ManagerDTO.builder()
                .managerId(manager.getManagerId())
                .firstName(manager.getFirstName())
                .lastName(manager.getLastName())
                .email(manager.getEmail())
                .phoneNumber(manager.getPhoneNumber())
                .status(manager.isStatus())
                .deptId(manager.getDeptId())
                .projectId(manager.getProjectId())
                .build();
    }

    public Manager toModel(ManagerDTO managerDTO){
        return Manager.builder()
                .managerId(managerDTO.getManagerId())
                .firstName(managerDTO.getFirstName())
                .lastName(managerDTO.getLastName())
                .email(managerDTO.getEmail())
                .phoneNumber(managerDTO.getPhoneNumber())
                .status(true)
                .deptId(managerDTO.getDeptId())
                .projectId(managerDTO.getProjectId())
                .build();
    }

    private void updateManagerDetails(Manager existingManagerDetails, ManagerDTO managerDTO){
        existingManagerDetails.setFirstName(managerDTO.getFirstName());
        existingManagerDetails.setLastName(managerDTO.getLastName());
        existingManagerDetails.setEmail(managerDTO.getEmail());
        existingManagerDetails.setPhoneNumber(managerDTO.getPhoneNumber());
        existingManagerDetails.setStatus(existingManagerDetails.isStatus());
        existingManagerDetails.setDeptId(managerDTO.getDeptId());
        existingManagerDetails.setProjectId(managerDTO.getProjectId());
    }

}
