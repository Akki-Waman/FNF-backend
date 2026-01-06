package com.sipl.ticket.core.dao.repository;


import com.sipl.ticket.core.dao.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

}
