package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(
            "SELECT t " +
                    "FROM Task t " +
                    "WHERE (:taskId IS NULL OR t.taskId = :taskId) " +
                    "AND (:subject IS NULL OR LOWER(t.subject) LIKE LOWER(CONCAT('%', :subject, '%'))) "

    )
    Page<Task> searchTasks(
            @Param("taskId") Long taskId,
            @Param("subject") String subject,
            Pageable pageable
    );

}
