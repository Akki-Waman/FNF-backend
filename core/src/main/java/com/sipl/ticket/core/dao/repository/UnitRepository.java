package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    List<Unit> findByIsActiveTrue();
}
