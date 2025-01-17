package com.virtusa.employee_service.dao;

import com.virtusa.employee_service.dto.*;
import com.virtusa.employee_service.feignClient.DepartmentFeignClient;
import com.virtusa.employee_service.feignClient.ManagerFeignClient;
import com.virtusa.employee_service.feignClient.ProjectFeignClient;
import com.virtusa.employee_service.feignClient.ValidationFeignClient;
import com.virtusa.employee_service.model.Employee;
import com.virtusa.employee_service.repository.EmployeeRepo;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EmployeeDAO {

    private final EmployeeRepo employeeRepo;
    private final DepartmentFeignClient departmentFeignClient;
    private final ManagerFeignClient managerFeignClient;
    private final ProjectFeignClient projectFeignClient;
    private final ValidationFeignClient validationFeignClient;

    private Logger logger = LoggerFactory.getLogger(EmployeeDAO.class);


    public EmployeeDAO(EmployeeRepo employeeRepo, DepartmentFeignClient departmentFeignClient, ManagerFeignClient managerFeignClient, ProjectFeignClient projectFeignClient, ValidationFeignClient validationFeignClient) {
        this.employeeRepo = employeeRepo;
        this.departmentFeignClient = departmentFeignClient;
        this.managerFeignClient = managerFeignClient;
        this.projectFeignClient = projectFeignClient;
        this.validationFeignClient = validationFeignClient;
    }

    //Save employee method
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO){
        ValidateEmployeeDTO validateEmployeeDTO = validationFeignClient.validateEmployee(employeeDTO).getBody();

        if(!validateEmployeeDTO.isValid()){
            log.error("Validation failed : {}", validateEmployeeDTO.getErrors());
            throw new IllegalArgumentException("validation failed : " + validateEmployeeDTO.getErrors());
        }

        Employee employee = toModel(employeeDTO);
        employee = employeeRepo.save(employee);

        DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(employeeDTO.getDeptId());
        ManagerDTO managerDTO = managerFeignClient.getManagerById(employeeDTO.getManagerId());
        ProjectDTO projectDTO = projectFeignClient.getProjectById(employeeDTO.getProjectId());
        log.info("Employee saved successfully with id : {}", employee.getEmployeeId());
        EmployeeDTO savedEmployee = toDTO(employee);
        savedEmployee.setDepartmentDTO(departmentDTO);
        savedEmployee.setManagerDTO(managerDTO);
        savedEmployee.setProjectDTO(projectDTO);
        return savedEmployee;

    }

    // finding employee by ID
    public EmployeeDTO findById(Long id){
        Employee employee = employeeRepo.findById(id).orElse(null);

        if(employee != null){
            DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(employee.getDeptId());
            ManagerDTO managerDTO = managerFeignClient.getManagerById(employee.getManagerId());
            ProjectDTO projectDTO = projectFeignClient.getProjectById(employee.getProjectId());
            EmployeeDTO employeeDTO = toDTO(employee);
            employeeDTO.setDepartmentDTO(departmentDTO);
            employeeDTO.setManagerDTO(managerDTO);
            employeeDTO.setProjectDTO(projectDTO);
            return employeeDTO;
        }
        return null;
    }

    // get all employees
    public List<EmployeeDTO> findAll(){
        List<Employee> employees = employeeRepo.findAll();
        return employees.stream().map(employee -> {
            DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(employee.getDeptId());
            ManagerDTO managerDTO = managerFeignClient.getManagerById(employee.getManagerId());
            ProjectDTO projectDTO = projectFeignClient.getProjectById(employee.getProjectId());
            EmployeeDTO employeeDTO = toDTO(employee);
            employeeDTO.setDepartmentDTO(departmentDTO);
            employeeDTO.setManagerDTO(managerDTO);
            employeeDTO.setProjectDTO(projectDTO);
            return employeeDTO;
        }).collect(Collectors.toList());
    }

    // update employee method
    public EmployeeDTO update(EmployeeDTO employeeDTO) {
        // Call validation service before updating
        ValidateEmployeeDTO validationResponse = validationFeignClient.validateEmployee(employeeDTO).getBody();

        if (!validationResponse.isValid()) {
            log.error("Validation failed: {}", validationResponse.getErrors());
            throw new IllegalArgumentException("Validation failed: " + validationResponse.getErrors());
        }

        Employee existingEmployee = employeeRepo.findById(employeeDTO.getEmployeeId()).orElse(null);

        if (existingEmployee != null) {
            // Update existing employee details
            updateEmployeeDetails(existingEmployee, employeeDTO);
            existingEmployee = employeeRepo.save(existingEmployee);

            // Fetch updated department details
            DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(employeeDTO.getDeptId());
            ManagerDTO managerDTO = managerFeignClient.getManagerById(employeeDTO.getManagerId());
            ProjectDTO projectDTO = projectFeignClient.getProjectById(employeeDTO.getProjectId());

            log.info("Employee updated successfully with id : {}", existingEmployee.getEmployeeId());
            EmployeeDTO updatedEmployee = toDTO(existingEmployee);
            updatedEmployee.setDepartmentDTO(departmentDTO);
            updatedEmployee.setManagerDTO(managerDTO);
            updatedEmployee.setProjectDTO(projectDTO);
            return updatedEmployee;
        } else {
            throw new IllegalArgumentException("Employee not found: " + employeeDTO.getEmployeeId());
        }
    }

    // delete employee i.e make it's status false
    public void deleteById(Long id) {
        Employee employee = employeeRepo.findById(id).orElse(null);
        if(employee != null){
            employee.setEmployeeStatus(false);
            employeeRepo.save(employee);
        } else {
            throw new IllegalArgumentException("Employee not found : " + employee.getEmployeeId());
        }
    }

    // list of employees present in department
//    @Retry(name = "default")
    public List<EmployeeDTO> getEmployeesByDeptId(Long deptId){
        List<Employee> employeeList = employeeRepo.findByDeptId(deptId);
//        logger.info("method getEmployeesByDeptId is called");
        DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(deptId);
        return employeeList.stream().map(employee -> {
            EmployeeDTO employeeDTO = toDTO(employee);
            ManagerDTO managerDTO = managerFeignClient.getManagerById(employeeDTO.getManagerId());
            ProjectDTO projectDTO = projectFeignClient.getProjectById(employeeDTO.getProjectId());
            employeeDTO.setDepartmentDTO(departmentDTO);
            employeeDTO.setManagerDTO(managerDTO);
            employeeDTO.setProjectDTO(projectDTO);
            return employeeDTO;
        }).toList();
    }

    public List<EmployeeDTO> getEmployeesByManagerId(Long managerId){
        List<Employee> employeeList = employeeRepo.findByManagerId(managerId);
        ManagerDTO managerDTO = managerFeignClient.getManagerById(managerId);
        return employeeList.stream().map(employee -> {
            EmployeeDTO employeeDTO = toDTO(employee);
            DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(employeeDTO.getDeptId());
            ProjectDTO projectDTO = projectFeignClient.getProjectById(employeeDTO.getProjectId());
            employeeDTO.setManagerDTO(managerDTO);
            employeeDTO.setDepartmentDTO(departmentDTO);
            employeeDTO.setProjectDTO(projectDTO);
            return employeeDTO;
        }).toList();
    }

    public List<EmployeeDTO> getEmployeesByProjectId(Long projectId){
        List<Employee> employeeList = employeeRepo.findByProjectId(projectId);
        ProjectDTO projectDTO = projectFeignClient.getProjectById(projectId);
        return employeeList.stream().map(employee -> {
            EmployeeDTO employeeDTO = toDTO(employee);
            DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(employeeDTO.getDeptId());
            ManagerDTO managerDTO = managerFeignClient.getManagerById(employeeDTO.getManagerId());
            employeeDTO.setProjectDTO(projectDTO);
            employeeDTO.setManagerDTO(managerDTO);
            employeeDTO.setDepartmentDTO(departmentDTO);
            return employeeDTO;
        }).toList();
    }

    public Employee toModel(EmployeeDTO employeeDTO){

        return Employee.builder()
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .email(employeeDTO.getEmail())
                .phoneNumber(employeeDTO.getPhoneNumber())
                .employeeStatus(true)
                .salary(employeeDTO.getSalary())
                .deptId(employeeDTO.getDeptId())
                .managerId(employeeDTO.getManagerId())
                .projectId(employeeDTO.getProjectId())
                .build();
    }

    public EmployeeDTO toDTO(Employee employee){
        return EmployeeDTO.builder()
                .employeeId(employee.getEmployeeId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .employeeStatus(employee.isEmployeeStatus())
                .salary(employee.getSalary())
                .deptId(employee.getDeptId())
                .managerId(employee.getManagerId())
                .projectId(employee.getProjectId())
                .build();
    }

    private void updateEmployeeDetails(Employee existingEmployee, EmployeeDTO employeeDTO){
        existingEmployee.setFirstName(employeeDTO.getFirstName());
        existingEmployee.setLastName(employeeDTO.getLastName());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setPhoneNumber(employeeDTO.getPhoneNumber());
        existingEmployee.setSalary(employeeDTO.getSalary());
        existingEmployee.setEmployeeStatus(existingEmployee.isEmployeeStatus());
        existingEmployee.setDeptId(employeeDTO.getDeptId());
        existingEmployee.setManagerId(employeeDTO.getManagerId());
        existingEmployee.setProjectId(employeeDTO.getProjectId());
    }

}
