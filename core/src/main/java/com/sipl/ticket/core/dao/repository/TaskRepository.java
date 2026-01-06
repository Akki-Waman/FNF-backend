package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Task;

import com.sipl.ticket.core.dao.entity.Ticket;
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
                    "SELECT t.status, COUNT(t.task_id) " +
                            "FROM tasks t " +
                            "GROUP BY t.status",
            nativeQuery = true
    )
    List<Object[]> getTaskSummary();


    @Query(
            value =
                    "SELECT t.status, COUNT(t.task_id) " +
                            "FROM tasks t " +
                            "JOIN tickets tk ON t.ticket_id = tk.ticket_id " +
                            "WHERE tk.assigned_to = :userId " +
                            "GROUP BY t.status",
            nativeQuery = true
    )
    List<Object[]> getUserTaskSummary(Long userId);
}




