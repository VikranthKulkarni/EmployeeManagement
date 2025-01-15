package com.virtusa.employee_service.dao;

import com.virtusa.employee_service.dto.DepartmentDTO;
import com.virtusa.employee_service.dto.EmployeeDTO;
import com.virtusa.employee_service.dto.ValidateEmployeeDTO;
import com.virtusa.employee_service.feignClient.DepartmentFeignClient;
import com.virtusa.employee_service.feignClient.ValidationFeignClient;
import com.virtusa.employee_service.model.Employee;
import com.virtusa.employee_service.repository.EmployeeRepo;
import lombok.extern.slf4j.Slf4j;
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
    private final ValidationFeignClient validationFeignClient;


    public EmployeeDAO(EmployeeRepo employeeRepo, DepartmentFeignClient departmentFeignClient, ValidationFeignClient validationFeignClient) {
        this.employeeRepo = employeeRepo;
        this.departmentFeignClient = departmentFeignClient;
        this.validationFeignClient = validationFeignClient;
    }

    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO){
        ValidateEmployeeDTO validateEmployeeDTO = validationFeignClient.validateEmployee(employeeDTO).getBody();

        if(!validateEmployeeDTO.isValid()){
            log.error("Validation failed : {}", validateEmployeeDTO.getErrors());
            throw new IllegalArgumentException("validation failed : " + validateEmployeeDTO.getErrors());
        }

        Employee employee = toModel(employeeDTO);
        employee = employeeRepo.save(employee);

        DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(employeeDTO.getDeptId());
        log.info("Employee saved successfully with id : {}", employee.getEmployeeId());
        EmployeeDTO savedEmployee = toDTO(employee);
        savedEmployee.setDepartmentDTO(departmentDTO);
        return savedEmployee;

    }

    public EmployeeDTO findById(Long id){
        Employee employee = employeeRepo.findById(id).orElse(null);

        if(employee != null){
            DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(employee.getDeptId());
            EmployeeDTO employeeDTO = toDTO(employee);
            employeeDTO.setDepartmentDTO(departmentDTO);
            return employeeDTO;
        }
        return null;
    }

    public List<EmployeeDTO> findAll(){
        List<Employee> employees = employeeRepo.findAll();
        return employees.stream().map(employee -> {
            DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(employee.getDeptId());
            EmployeeDTO employeeDTO = toDTO(employee);
            employeeDTO.setDepartmentDTO(departmentDTO);
            return employeeDTO;
        }).collect(Collectors.toList());
    }

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

            log.info("Employee updated successfully with id : {}", existingEmployee.getEmployeeId());
            EmployeeDTO updatedEmployee = toDTO(existingEmployee);
            updatedEmployee.setDepartmentDTO(departmentDTO);
            return updatedEmployee;
        } else {
            throw new IllegalArgumentException("Employee not found: " + employeeDTO.getEmployeeId());
        }
    }

    public void deleteById(Long id) {
        Employee employee = employeeRepo.findById(id).orElse(null);
        if(employee != null){
            employee.setEmployeeStatus(false);
        } else {
            throw new IllegalArgumentException("Employee not found : " + employee.getEmployeeId());
        }
    }

    public List<EmployeeDTO> getEmployeesByDeptId(Long deptId){
        List<Employee> employeeList = employeeRepo.findByDeptId(deptId);
        DepartmentDTO departmentDTO = departmentFeignClient.getDeptByID(deptId);
        return employeeList.stream().map(employee -> {
            EmployeeDTO employeeDTO = toDTO(employee);
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
    }

}
