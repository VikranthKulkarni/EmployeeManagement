package com.virtusa.employee_service.controller;

import com.virtusa.employee_service.dto.EmployeeDTO;
import com.virtusa.employee_service.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;


    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> saveEmployee(@RequestBody EmployeeDTO employeeDTO){
        EmployeeDTO savedEmployee = employeeService.saveEmployee(employeeDTO);
        return ResponseEntity.ok(savedEmployee);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(){
        return ResponseEntity.ok(employeeService.findAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id){
        EmployeeDTO employeeDTO = employeeService.findById(id);
        return ResponseEntity.ok(employeeDTO);
    }

    @PutMapping
    public ResponseEntity<EmployeeDTO> updateEmployeeDetails(@RequestBody EmployeeDTO employeeDTO){
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(employeeDTO);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/byDeptId/{deptId}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDeptId(@PathVariable("deptId") Long deptId){
        return ResponseEntity.ok(employeeService.getEmployeesByDeptId(deptId));
    }
}
