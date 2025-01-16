package com.virtusa.manager_service.service;

import com.virtusa.manager_service.dao.ManagerDAO;
import com.virtusa.manager_service.dto.EmployeeDTO;
import com.virtusa.manager_service.dto.ManagerDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {

    private final ManagerDAO managerDAO;

    public ManagerService(ManagerDAO managerDAO) {
        this.managerDAO = managerDAO;
    }

    public ManagerDTO saveManager(ManagerDTO managerDTO){
        return managerDAO.saveManager(managerDTO);
    }

    public ManagerDTO findManagerById(Long id){
        return managerDAO.findManagerById(id);
    }

    public List<ManagerDTO> findAllManagers(){
        return managerDAO.findAllManagers();
    }

    public ManagerDTO updateManager(ManagerDTO managerDTO){
        return managerDAO.updateManager(managerDTO);
    }

    public void deleteManagerById(Long id){
        managerDAO.deleteManagerById(id);
    }

    public List<EmployeeDTO> getEmployeesByManagerId(Long managerId){
        return managerDAO.getEmployeesByManagerId(managerId);
    }

    public List<ManagerDTO> getManagersByDeptId(Long deptId){
        return managerDAO.getManagersByDeptId(deptId);
    }

}
