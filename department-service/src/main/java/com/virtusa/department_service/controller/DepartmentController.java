package com.virtusa.department_service.controller;

import com.virtusa.department_service.dto.DepartmentDTO;
import com.virtusa.department_service.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;


    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<DepartmentDTO> saveDept(@RequestBody DepartmentDTO departmentDTO){
        DepartmentDTO savedDept = departmentService.saveDept(departmentDTO);
        return ResponseEntity.ok(savedDept);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDept(){
        List<DepartmentDTO> departments = departmentService.findAllDept();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDeptByID(@PathVariable Long id){
        DepartmentDTO departmentDTO = departmentService.findDeptByID(id);
        return ResponseEntity.ok(departmentDTO);
    }

    @PutMapping
    public ResponseEntity<DepartmentDTO> updateDept(@RequestBody DepartmentDTO departmentDTO){
        DepartmentDTO updatedDept = departmentService.updateDept(departmentDTO);
        return ResponseEntity.ok(updatedDept);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDept(@PathVariable Long id){
        departmentService.deleteDept(id);
        return ResponseEntity.noContent().build();
    }

}
