package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Ticket;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @EntityGraph(attributePaths = {
            "assignedTo",
            "location",
            "department",
            "clientProducts",
            "branch",
            "service",
            "contact"
    })
    @Query("FROM Ticket t WHERE t.ticketId = :ticketId")
    Optional<Ticket> findByIdWithAllDetails(@Param("ticketId") Long ticketId);
}
