package com.virtusa.employee_service.service;

import com.virtusa.employee_service.dao.EmployeeDAO;
import com.virtusa.employee_service.dto.EmployeeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private  final EmployeeDAO employeeDAO;


    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        return employeeDAO.saveEmployee(employeeDTO);
    }

    public EmployeeDTO findById(Long id) {
        return employeeDAO.findById(id);
    }

    public List<EmployeeDTO> findAllEmployees(){
        return employeeDAO.findAll();
    }

    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO){
        return employeeDAO.update(employeeDTO);
    }

    public void deleteEmployee(Long id) {
        employeeDAO.deleteById(id);
    }

    public List<EmployeeDTO> getEmployeesByDeptId(Long deptId){
        return employeeDAO.getEmployeesByDeptId(deptId);
    }

    public List<EmployeeDTO> getEmployeesByManagerId(Long managerId){
        return employeeDAO.getEmployeesByManagerId(managerId);
    }

    public List<EmployeeDTO> getEmployeesByProjectId(Long projectId){
        return employeeDAO.getEmployeesByProjectId(projectId);
    }
}
