package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    List<Unit> findByIsActiveTrue();


    @Query("From Unit u where u.unitId = :unitId AND u.isActive = true")
    Optional<Unit> findActiveById(Long unitId);
}
