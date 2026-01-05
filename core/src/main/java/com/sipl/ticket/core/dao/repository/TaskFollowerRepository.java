package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TaskFollower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskFollowerRepository extends JpaRepository<TaskFollower, Long> {

    @Query("SELECT tf FROM TaskFollower tf WHERE tf.task.taskId = :taskId")
    List<TaskFollower> findByTaskTaskId(@Param("taskId") Long taskId);

}
