package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TaskAssignee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAssigneeRepository extends JpaRepository<TaskAssignee, Long> {
}
