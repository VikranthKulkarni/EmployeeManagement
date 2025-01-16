package com.virtusa.department_service.feignClient;

import com.virtusa.department_service.dto.ManagerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "manager-service")
public interface ManagerFeignClient {

    @GetMapping("/api/managers/department/{deptId}")
    ResponseEntity<List<ManagerDTO>> getManagersByDeptId(@PathVariable("deptId") Long deptId);

}
