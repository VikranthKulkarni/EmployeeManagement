package com.virtusa.department_service.service;

import com.virtusa.department_service.dao.DepartmentDAO;
import com.virtusa.department_service.dto.DepartmentDTO;
import com.virtusa.department_service.dto.EmployeeDTO;
import com.virtusa.department_service.dto.ManagerDTO;
import com.virtusa.department_service.dto.ProjectDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentDAO departmentDAO;


    public DepartmentService(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    public DepartmentDTO saveDept(DepartmentDTO departmentDTO){
        return departmentDAO.saveDept(departmentDTO);
    }

    public DepartmentDTO findDeptByID (Long id) {
        return departmentDAO.findDeptByID(id);
    }

    public List<DepartmentDTO> findAllDept(){
        return departmentDAO.findAllDept();
    }

    public DepartmentDTO updateDept(DepartmentDTO departmentDTO){
        return departmentDAO.updateDept(departmentDTO);
    }

    public void deleteDept(Long id){
        departmentDAO.deleteDept(id);
    }

    public List<EmployeeDTO> findEmployeesByDeptId(Long deptId){
        return departmentDAO.findEmployeesByDeptId(deptId);
    }

    public List<ManagerDTO> getManagersByDeptId(Long deptId){
        return departmentDAO.getManagersByDeptId(deptId);
    }

    public List<ProjectDTO> getProjectsByDeptId(Long deptId){
        return departmentDAO.getProjectsByDeptId(deptId);
    }

}
