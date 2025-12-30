package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByDepartmentNameIgnoreCaseAndIsDeletedFalse(String departmentName);

    boolean existsByDepartmentNameIgnoreCaseAndDepartmentIdNotAndIsDeletedFalse(
            String departmentName, Long departmentId
    );

    List<Department> findByIsDeletedFalse();

    @Query("SELECT d " +
            "FROM Department d " +
            "WHERE d.isActive = true " +
            "AND (:departmentId IS NULL OR d.departmentId = :departmentId)")
    Page<Department> searchByDepartmentId(
            @Param("departmentId") Long departmentId,
            Pageable pageable
    );


}
