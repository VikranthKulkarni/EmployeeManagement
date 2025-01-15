package com.virtusa.department_service.dao;

import com.virtusa.department_service.dto.DepartmentDTO;
import com.virtusa.department_service.dto.EmployeeDTO;
import com.virtusa.department_service.feingClient.EmployeeFeignClient;
import com.virtusa.department_service.model.Department;
import com.virtusa.department_service.repository.DepartmentRepo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepartmentDAO {

    private final DepartmentRepo departmentRepo;
    private final EmployeeFeignClient employeeFeignClient;


    public DepartmentDAO(DepartmentRepo departmentRepo, EmployeeFeignClient employeeFeignClient) {
        this.departmentRepo = departmentRepo;
        this.employeeFeignClient = employeeFeignClient;
    }

    public DepartmentDTO saveDept(DepartmentDTO departmentDTO){
        Department dept = toModel(departmentDTO);
        dept = departmentRepo.save(dept);
        return toDTO(dept);
    }

    public DepartmentDTO findDeptByID(Long id){
        Department department = departmentRepo.findById(id).orElse(null);
        return department != null ? toDTO(department) : null;
    }

    public List<DepartmentDTO> findAllDept(){
        List<Department> departments = departmentRepo.findAll();
        return departments.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public DepartmentDTO updateDept(DepartmentDTO departmentDTO){
        Department existingDepartment = departmentRepo.findById(departmentDTO.getDeptId()).orElse(null);
        if(existingDepartment != null){
            updateDeptDetails(existingDepartment, departmentDTO);
            existingDepartment = departmentRepo.save(existingDepartment);
            return toDTO(existingDepartment);
        } else {
            throw new IllegalArgumentException("Department not found with id : " + departmentDTO.getDeptId());
        }
    }

    public void deleteDept(Long id) {
        Department department = departmentRepo.findById(id).orElse(null);
        if(department != null) {
            departmentRepo.delete(department);
        } else {
            throw new IllegalArgumentException("Department not found with id :" + id);
        }
    }

    public DepartmentDTO toDTO(Department department){
        return DepartmentDTO.builder()
                .deptId(department.getDeptId())
                .deptName(department.getDeptName())
                .build();
    }

    public Department toModel(DepartmentDTO departmentDTO){
        return Department.builder()
                .deptName(departmentDTO.getDeptName())
                .build();
    }

    public void updateDeptDetails(Department existingDepartment, DepartmentDTO departmentDTO){
        existingDepartment.setDeptId(departmentDTO.getDeptId());
        existingDepartment.setDeptName(departmentDTO.getDeptName());
    }

}
