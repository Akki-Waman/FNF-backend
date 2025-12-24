package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.WorkflowSteps;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowStepsRepository extends JpaRepository<WorkflowSteps,Integer> {
    @Query(
            "SELECT ws FROM WorkflowSteps ws WHERE ws.workflowDefinition.workFlowDefinitionId = :definitionId AND ws.stepOrder = 1")
    Optional<WorkflowSteps> findFirstStepByDefinitionId(@Param("definitionId") Integer definitionId);

    @Query("SELECT ws FROM WorkflowSteps ws " +
            "WHERE ws.workflowDefinition.workFlowDefinitionId = :definitionId " +
            "ORDER BY ws.stepOrder ASC")
    List<WorkflowSteps> findStepsByDefinitionId(@Param("definitionId") Integer definitionId);

    @Query(
            "SELECT ws FROM WorkflowSteps ws WHERE ws.workflowDefinition.workFlowDefinitionId = :definitionId AND ws.stepOrder = :nextOrder")
    Optional<WorkflowSteps> findNextStep(
            @Param("definitionId") Integer definitionId, @Param("nextOrder") Integer nextOrder);

    @Query(
            "SELECT ws FROM WorkflowSteps ws WHERE ws.stepOrder = :stepOrder AND ws.workflowDefinition.workFlowDefinitionId = :workflowDefinitionId AND ws.role.id = :roleId")
    Optional<WorkflowSteps> findWorkflowSteps(
            @Param("stepOrder") Integer stepOrder,
            @Param("workflowDefinitionId") Integer workflowDefinitionId,
            @Param("roleId") Long roleId);

    @Query("From WorkflowSteps ws where ws.workFlowStepsId=?1")
    WorkflowSteps findWorkflowStepsId(Integer workFlowStepsId);

    @Query(
            "SELECT CASE WHEN COUNT(ws) > 0 THEN true ELSE false END FROM WorkflowSteps ws WHERE ws.stepOrder = :stepOrder AND ws.workflowDefinition.workFlowDefinitionId = :workflowDefinitionId AND ws.role.id = :roleId")
    boolean existsByStepOrderAndWorkflowDefinitionIdAndRoleId(
            @Param("stepOrder") Integer stepOrder,
            @Param("workflowDefinitionId") Integer workflowDefinitionId,
            @Param("roleId") Long roleId);

    @Query(
            "SELECT ws FROM WorkflowSteps ws "
                    + "WHERE (:stepName IS NULL OR ws.stepName LIKE %:stepName%) "
                    + "AND (:roleId IS NULL OR ws.role.id = :roleId) "
                    + "AND (:finalApprover IS NULL OR ws.isFinalApprover = :finalApprover) "
                    + "AND (:stepOrder IS NULL OR ws.stepOrder = :stepOrder) "
                    + "AND (:workFlowDefinitionId IS NULL OR ws.workflowDefinition.workFlowDefinitionId = :workFlowDefinitionId) "
                    + "AND (:workFlowStepsId IS NULL OR ws.workFlowStepsId = :workFlowStepsId)")
    Page<WorkflowSteps> findBySearchQuery(
            @Param("stepName") String stepName,
            @Param("roleId") Long roleId,
            @Param("finalApprover") Boolean finalApprover,
            @Param("stepOrder") Integer stepOrder,
            @Param("workFlowDefinitionId") Integer workFlowDefinitionId,
            @Param("workFlowStepsId") Integer workFlowStepsId,
            Pageable pageable);
}
