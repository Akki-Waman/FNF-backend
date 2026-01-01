package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
