package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Task;

import com.sipl.ticket.core.dao.entity.Ticket;
import com.sipl.ticket.core.dto.response.TaskStatusCountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(
            "SELECT t " +
                    "FROM Task t " +
                    "WHERE (:ticketId IS NULL OR t.ticket.ticketId = :ticketId) " +
                    "AND ( " +
                    "   :query IS NULL " +
                    "   OR CAST(t.taskId AS string) = :query " +
                    "   OR LOWER(t.subject) LIKE LOWER(CONCAT('%', :query, '%')) " +
                    ")"
    )
    Page<Task> searchTasks(
            @Param("ticketId") Long ticketId,
            @Param("query") String query,
            Pageable pageable
    );


    @Query(
            "SELECT new com.sipl.ticket.core.dto.response.TaskStatusCountDto(" +
                    "m.columnValue, m.valueDesc, COUNT(t)) " +
                    "FROM Masters m " +
                    "LEFT JOIN Task t ON t.status = m.columnValue " +
                    "WHERE m.columnCode = 1 " +
                    "AND m.tblName = 'tasks' " +
                    "AND m.isActive = true " +
                    "GROUP BY m.columnValue, m.valueDesc, m.sequence " +
                    "ORDER BY m.sequence"
    )
    List<TaskStatusCountDto> getOverallTaskSummary();


    @Query(
            "SELECT new com.sipl.ticket.core.dto.response.TaskStatusCountDto(" +
                    "   m.columnValue, " +
                    "   m.valueDesc, " +
                    "   COUNT(DISTINCT t) " +
                    ") " +
                    "FROM Masters m " +
                    "LEFT JOIN Task t " +
                    "   ON t.status = m.columnValue " +
                    "LEFT JOIN TaskAssignee ta " +
                    "   ON ta.task = t AND ta.user.id = :userId " +
                    "WHERE m.columnCode = 1 " +
                    "  AND m.tblName = 'tasks' " +
                    "  AND m.isActive = true " +
                    "GROUP BY m.columnValue, m.valueDesc, m.sequence " +
                    "ORDER BY m.sequence"
    )
    List<TaskStatusCountDto> getUserTaskSummary(
            @Param("userId") Long userId
    );


}




