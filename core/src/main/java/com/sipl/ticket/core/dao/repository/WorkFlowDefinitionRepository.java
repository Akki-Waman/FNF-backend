package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.WorkFlowDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkFlowDefinitionRepository extends JpaRepository<WorkFlowDefinition,Integer> {
    @Query(
            "SELECT w FROM WorkFlowDefinition w where w.entityType = :entityType and w.isActive = true ")
    Optional<WorkFlowDefinition> findByEntityType(@Param("entityType") String entityType);


    @Query(
            "SELECT w FROM WorkFlowDefinition w "
                    + "WHERE "
                    + " (:name IS NULL OR LOWER(w.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
                    + "AND (:entityType IS NULL OR LOWER(w.entityType) LIKE LOWER(CONCAT('%', :entityType, '%')))")
    Page<WorkFlowDefinition> searchWorkFlowDefinitions(
            @Param("name") String name, @Param("entityType") String entityType, Pageable pageable);

    @Query(
            "SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM WorkFlowDefinition w WHERE w.entityType = :entityType")
    boolean existsByEntityType(@Param("entityType") String entityType);

    @Query(
            "SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM WorkFlowDefinition w WHERE w.name = :workFlowName")
    boolean existsByWorkFlowName(@Param("workFlowName") String workFlowName);

    @Query(
            "SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM WorkFlowDefinition w WHERE w.entityType = :entityType AND w.workFlowDefinitionId <> :id")
    boolean existsByEntityTypeAndNotId(
            @Param("entityType") String entityType, @Param("id") Integer id);

    @Query(
            "SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM WorkFlowDefinition w WHERE w.name = :workFlowName AND w.workFlowDefinitionId <> :id")
    boolean existsByWorkFlowNameAndNotId(
            @Param("workFlowName") String workFlowName, @Param("id") Integer id);

    List<WorkFlowDefinition> findByIsActiveTrue();
}
