package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.WorkflowNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowNotificationRepository extends JpaRepository<WorkflowNotification,Integer> {
    @Query(
            "SELECT wn FROM WorkflowNotification wn "
                    + "WHERE (:status IS NULL OR wn.status = :status) "
                    + "AND (:notificationType IS NULL OR wn.notificationType = :notificationType) "
                    + "AND (:notificationId IS NULL OR wn.workflowNotificationId = :notificationId)")
    Page<WorkflowNotification> findBySearchQuery(
            @Param("status") String status,
            @Param("notificationType") String notificationType,
            @Param("notificationId") Integer notificationId,
            Pageable pageable);
}
