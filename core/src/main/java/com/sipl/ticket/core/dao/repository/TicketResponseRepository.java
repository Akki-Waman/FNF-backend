package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Ticket;
import com.sipl.ticket.core.dao.entity.TicketResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketResponseRepository extends JpaRepository<TicketResponse, Long> {
    @Query("FROM TicketResponse t WHERE t.ticketResponseId = :ticketResponseId")
    Optional<TicketResponse> findByIdWithAllDetails( @Param("ticketResponseId") Long ticketResponseId);
}
