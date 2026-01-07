package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.MovementFlowMapping;
import com.sipl.ticket.core.dao.entity.OperationalUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationalUnitRepository extends JpaRepository<OperationalUnit, Long> {
}
