package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Task;

import com.sipl.ticket.core.dao.entity.Ticket;
import com.sipl.ticket.core.dto.response.TaskStatusCountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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
                    "   m.columnValue, " +
                    "   m.valueDesc, " +
                    "   COUNT(DISTINCT t), " +
                    "   COUNT(DISTINCT CASE WHEN ta.user.id = :userId THEN t END) " +
                    ") " +
                    "FROM Masters m " +
                    "LEFT JOIN Task t " +
                    "   ON t.status = m.columnValue " +
                    "LEFT JOIN TaskAssignee ta " +
                    "   ON ta.task = t " +
                    "WHERE m.columnCode = 1 " +
                    "  AND m.tblName = 'tasks' " +
                    "  AND m.isActive = true " +
                    "GROUP BY m.columnValue, m.valueDesc, m.sequence " +
                    "ORDER BY m.sequence"
    )
    List<TaskStatusCountDto> getTaskStatusSummaryWithUserAssignment(
            @Param("userId") Long userId
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Task t SET t.isDeleted = true WHERE t.id IN (:ids)")
    int softDeleteByIds(@Param("ids") List<Long> ids);

    @Query(
            "SELECT t FROM Task t " +
                    "WHERE (t.isDeleted = false OR t.isDeleted IS NULL) " +
                    "AND ( :search IS NULL OR :search = '' " +
                    "   OR LOWER(t.subject) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR STR(t.taskId) LIKE CONCAT('%', :search, '%') " +
                    ") " +
                    "AND ( :statuses IS NULL OR t.status IN (:statuses) ) " +
                    "AND ( :priorities IS NULL OR t.priority IN (:priorities) ) " +
                    "AND ( :from IS NULL OR t.createdTime IS NULL OR t.createdTime >= :from ) " +
                    "AND ( :to IS NULL OR t.createdTime IS NULL OR t.createdTime <= :to ) "
    )
    List<Task> searchTasksWithFilters(
            @Param("search") String search,
            @Param("statuses") List<Integer> statuses,
            @Param("priorities") List<Integer> priorities,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}




