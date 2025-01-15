package com.virtusa.department_service.repository;

import com.virtusa.department_service.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepo extends JpaRepository<Department, Long> {

}
