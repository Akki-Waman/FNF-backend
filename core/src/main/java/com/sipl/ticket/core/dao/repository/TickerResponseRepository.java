package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TicketResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TickerResponseRepository extends JpaRepository<TicketResponse, Long> {
}
