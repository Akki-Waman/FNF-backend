package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByName(String name);

    List<Department> findByIsDeletedFalse();


}
