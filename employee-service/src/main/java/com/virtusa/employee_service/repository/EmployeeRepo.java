package com.virtusa.employee_service.repository;

import com.virtusa.employee_service.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    List<Employee> findByDeptId(Long deptId);

}
