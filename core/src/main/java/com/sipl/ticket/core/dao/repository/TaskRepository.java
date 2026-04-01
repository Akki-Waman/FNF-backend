package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Task;

import com.sipl.ticket.core.dto.response.TaskResponseDTO;
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
import java.util.Set;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(
            "SELECT t " +
                    "FROM Task t " +
                    "WHERE (:ticketId IS NULL OR t.ticket.ticketId = :ticketId) " +
                    "AND ( :branchId IS NULL OR t.branch.branchId = :branchId ) " +
                    "AND ( :taskStatus IS NULL OR t.status = :taskStatus ) " +   // 🔥 new line
                    "AND ( " +
                    "   :query IS NULL " +
                    "   OR CAST(t.taskId AS string) = :query " +
                    "   OR LOWER(t.subject) LIKE LOWER(CONCAT('%', :query, '%')) " +
                    ")"
    )
    Page<Task> searchTasks(
            @Param("ticketId") Long ticketId,
            @Param("branchId") Integer branchId,
            @Param("query") String query,
            @Param("taskStatus") Integer taskStatus,
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

    @Query("SELECT t.taskId FROM Task t WHERE t.isDeleted = false")
    List<Long> findAllActiveTaskIds();

@Query("SELECT new com.sipl.ticket.core.dto.response.TaskResponseDTO(" +
        "t.taskId, " +
        "tk.ticketId, " +
        "t.subject, " +
        "tk.subject, " +
        "pri.valueDesc, " +
        "sts.valueDesc, " +
        "t.startDate, " +
        "t.dueDate, " +
        "t.createdTime" +
        ") " +
        "FROM Task t " +
        "JOIN t.ticket tk " +
        "LEFT JOIN Masters pri ON pri.columnValue = t.priority AND pri.columnCode = 6 AND pri.isActive = true " +
        "LEFT JOIN Masters sts ON sts.columnValue = t.status AND sts.columnCode = 1 AND sts.isActive = true " +
        "WHERE t.isDeleted = false " +
        "AND t.createdTime >= :startDateTime " +
        "AND t.createdTime <= :endDateTime")
List<TaskResponseDTO> findTask(
        @Param("startDateTime") LocalDateTime startDateTime,
        @Param("endDateTime") LocalDateTime endDateTime
);

    @Query(" SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END FROM Task t "+
    "WHERE t.ticket.ticketId = :ticketId  AND t.isDeleted = false AND t.status <> :status ")
    boolean existsByTicketIdAndStatusNot(
            @Param("ticketId") Long ticketId,
            @Param("status") Integer status
    );

    Page<Task> findByTaskIdIn(Set<Long> taskIds, Pageable pageable);

}




