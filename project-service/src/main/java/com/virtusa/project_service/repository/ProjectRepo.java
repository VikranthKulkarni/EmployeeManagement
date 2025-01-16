package com.virtusa.project_service.repository;

import com.virtusa.project_service.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Long> {

    List<Project> findByDeptId(Long deptId);

}
