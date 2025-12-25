package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByDepartmentNameIgnoreCaseAndIsDeletedFalse(String departmentName);

    boolean existsByDepartmentNameIgnoreCaseAndDepartmentIdNotAndIsDeletedFalse(
            String departmentName, Long departmentId
    );

    List<Department> findByIsDeletedFalse();
}
