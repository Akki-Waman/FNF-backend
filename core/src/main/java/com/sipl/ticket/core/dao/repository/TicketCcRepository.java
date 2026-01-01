package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TicketCc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketCcRepository extends JpaRepository<TicketCc, Long> {
}
