package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Movements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends JpaRepository<Movements, Integer> {
    boolean existsBySourceAndDestination(Integer source, Integer destination);

    boolean existsByMovementDescriptionIgnoreCase(String desc);

    boolean existsByMovementDescriptionIgnoreCaseAndMovementIdNot(String desc, Integer id);
}
