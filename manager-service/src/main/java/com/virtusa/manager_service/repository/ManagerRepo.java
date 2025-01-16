package com.virtusa.manager_service.repository;

import com.virtusa.manager_service.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagerRepo extends JpaRepository<Manager, Long> {

    List<Manager> findByDeptId(Long deptId);

}
