package com.sipl.ticket.core.dao.repository;

import com.opencsv.bean.CsvToBean;
import com.sipl.ticket.core.dao.entity.Ticket;
import com.sipl.ticket.core.dao.entity.TicketTag;
import com.sipl.ticket.core.dto.response.TicketTagResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketTagRepository extends JpaRepository<TicketTag, Long> {
    List<TicketTag> findByTicket(Ticket ticket);

    @Query("From TicketTag t WHERE t.ticket.ticketId = :ticketId")
    List<TicketTag> findByTicketId(@Param("ticketId") Long ticketId);
}
