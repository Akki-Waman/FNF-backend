package com.sipl.ticket.core.dao.repository;

import com.opencsv.bean.CsvToBean;
import com.sipl.ticket.core.dao.entity.Task;
import com.sipl.ticket.core.dao.entity.TaskAssignee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskAssigneeRepository extends JpaRepository<TaskAssignee, Long> {

    List<TaskAssignee> findByTaskTaskId(Long taskId);

    List<TaskAssignee> findByTask(Task task);
}
