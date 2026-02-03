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
            "SELECT wi FROM WorkflowInstance wi " +
                    "JOIN wi.currentStep ws " +
                    "LEFT JOIN ws.role r " +
                    "LEFT JOIN wi.workflow w " +
                    "WHERE (:entityType IS NULL OR wi.entityType = :entityType) " +
                    "AND (:status IS NULL OR wi.workFlowStatus = :status) " +
                    "AND ( " +
                    "   (ws.assignmentMode = 2 AND wi.assignedUser.id = :userId) " +     // USER
                    "   OR " +
                    "   (ws.assignmentMode = 1 AND r.id = :roleId) " +                   // ROLE
                    ")"
    )
    Page<WorkflowInstance> findByFilters(
            @Param("entityType") String entityType,
            @Param("status") Integer status,
            @Param("userId") Long userId,
            @Param("roleId") Integer roleId,
            Pageable pageable
    );

}
