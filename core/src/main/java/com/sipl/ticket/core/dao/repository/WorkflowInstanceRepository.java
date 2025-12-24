package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.WorkflowInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstance,Integer> {
    @Query(
            "SELECT w FROM WorkflowInstance w "
                    + "WHERE (:entityType IS NULL OR w.entityType = :entityType) "
                    + "AND (:status IS NULL OR w.workFlowStatus = :status) "
                    + "AND (:roleId IS NULL OR w.currentStep.role.id = :roleId)")
    Page<WorkflowInstance> findByEntityTypeAndStatusAndRole(
            @Param("entityType") String entityType,
            @Param("status") Integer status,
            @Param("roleId") Long roleId,
            Pageable pageable);
}
