package com.virtusa.manager_service.controller;

import com.virtusa.manager_service.dto.EmployeeDTO;
import com.virtusa.manager_service.dto.ManagerDTO;
import com.virtusa.manager_service.service.ManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping
    public ResponseEntity<ManagerDTO> saveManager(@RequestBody ManagerDTO managerDTO){
        ManagerDTO manager = managerService.saveManager(managerDTO);
        return ResponseEntity.ok(manager);
    }

    @GetMapping
    public ResponseEntity<List<ManagerDTO>> getAllManagers(){
        List<ManagerDTO> managerList = managerService.findAllManagers();
        return ResponseEntity.ok(managerList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerDTO> getManagerById(@PathVariable Long id){
        ManagerDTO managerDTO = managerService.findManagerById(id);
        return ResponseEntity.ok(managerDTO);
    }

    @PutMapping
    public ResponseEntity<ManagerDTO> updateManager(@RequestBody ManagerDTO managerDTO){
        ManagerDTO updatedManagerDetails = managerService.updateManager(managerDTO);
        return ResponseEntity.ok(updatedManagerDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManagerById(@PathVariable Long id){
        managerService.deleteManagerById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("EmpByManager/{managerId}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByManagerId(@PathVariable Long managerId){
        List<EmployeeDTO> employees = managerService.getEmployeesByManagerId(managerId);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("department/{deptId}")
    public ResponseEntity<List<ManagerDTO>> getManagersByDeptId(@PathVariable("deptId") Long deptId){
        return ResponseEntity.ok(managerService.getManagersByDeptId(deptId));
    }

}
