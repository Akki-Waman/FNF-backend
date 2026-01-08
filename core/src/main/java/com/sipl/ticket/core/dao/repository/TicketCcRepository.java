package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Ticket;
import com.sipl.ticket.core.dao.entity.TicketCc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketCcRepository extends JpaRepository<TicketCc, Long> {
    List<TicketCc> findByTicket(Ticket ticket);

    @Query("SELECT tc.cc FROM TicketCc tc WHERE tc.ticket.ticketId = :ticketId")
    List<String> findCcEmailsByTicketId(@Param("ticketId") Long ticketId);
}
