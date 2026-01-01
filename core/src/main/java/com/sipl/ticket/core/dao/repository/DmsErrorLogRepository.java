package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.DmsErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DmsErrorLogRepository extends JpaRepository<DmsErrorLog, Long> {
}
