package com.sipl.ticket.core.dao.repository;


import com.sipl.ticket.core.dao.entity.ActivityLog;
import com.sipl.ticket.core.dao.entity.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    @Query("SELECT a FROM ActivityLog a ORDER BY a.createdTime DESC")
    List<ActivityLog> findLatestLogs(Pageable pageable);

    @Query("SELECT a FROM ActivityLog a WHERE a.performedBy = :user ORDER BY a.createdTime DESC")
    List<ActivityLog> findLatestLogsByUser(
            @Param("user") Users user,
            Pageable pageable
    );

}
