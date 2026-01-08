package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.EmailNotificationLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailNotificationLogsRepository
        extends JpaRepository<EmailNotificationLogs, Long> {
}
