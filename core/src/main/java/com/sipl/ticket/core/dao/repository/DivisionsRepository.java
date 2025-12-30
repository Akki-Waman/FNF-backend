package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Divisions;
import net.bytebuddy.implementation.bytecode.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DivisionsRepository extends JpaRepository<Divisions, Long> {
}
