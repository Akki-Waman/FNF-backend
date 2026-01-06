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
            value =
                    "SELECT " +
                            "  m.column_value   AS statusId, " +
                            "  m.value_desc     AS statusName, " +
                            "  COUNT(t.task_id) AS totalCount " +
                            "FROM masters m " +
                            "LEFT JOIN tasks t " +
                            "  ON t.status = m.column_value " +
                            "WHERE m.column_code = 1 " +
                            "  AND m.tbl_name = 'tasks' " +
                            "  AND m.is_active = 1 " +
                            "GROUP BY m.column_value, m.value_desc, m.sequence " +
                            "ORDER BY m.sequence",
            nativeQuery = true
    )
    List<TaskStatusCountDto> getOverallTaskSummary();

    @Query(
            value =
                    "SELECT " +
                            "  m.column_value   AS statusId, " +
                            "  m.value_desc     AS statusName, " +
                            "  COUNT(t.task_id) AS totalCount " +
                            "FROM masters m " +
                            "LEFT JOIN tasks t " +
                            "  ON t.status = m.column_value " +
                            "LEFT JOIN tickets tk " +
                            "  ON tk.ticket_id = t.ticket_id " +
                            " AND tk.assigned_to = :userId " +
                            "WHERE m.column_code = 1 " +
                            "  AND m.tbl_name = 'tasks' " +
                            "  AND m.is_active = 1 " +
                            "GROUP BY m.column_value, m.value_desc, m.sequence " +
                            "ORDER BY m.sequence",
            nativeQuery = true
    )
    List<TaskStatusCountDto> getUserTaskSummary(
            @Param("userId") Long userId
    );

}




