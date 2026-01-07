package com.sipl.ticket.core.dao.repository;

import com.opencsv.bean.CsvToBean;
import com.sipl.ticket.core.dao.entity.Task;
import com.sipl.ticket.core.dao.entity.TaskAssignee;
import com.sipl.ticket.core.dao.entity.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskTagRepository
        extends JpaRepository<TaskTag, Long> {

    List<TaskTag> findByTaskTaskId(Long taskId);

    List<TaskTag> findByTask(Task task);
}
