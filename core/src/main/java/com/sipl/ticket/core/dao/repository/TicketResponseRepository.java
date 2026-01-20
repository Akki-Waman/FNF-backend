package com.sipl.ticket.core.dao.repository;

import com.opencsv.bean.CsvToBean;
import com.sipl.ticket.core.dao.entity.Ticket;
import com.sipl.ticket.core.dao.entity.TicketResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketResponseRepository extends JpaRepository<TicketResponse, Long> {
    @Query("FROM TicketResponse t WHERE t.ticketResponseId = :ticketResponseId")
    Optional<TicketResponse> findByIdWithAllDetails( @Param("ticketResponseId") Long ticketResponseId);

    @Query("SELECT tr.createdTime " +
            "    FROM TicketResponse tr " +
            "    WHERE tr.ticket.ticketId = :ticketId " +
            "    ORDER BY tr.createdTime DESC")
    List<LocalDateTime> findLastReplyTime(@Param("ticketId") Long ticketId);

    @Query(
            "SELECT tr FROM TicketResponse tr " +
                    "WHERE tr.ticket IN :tickets"
    )
    List<TicketResponse> findByTicketIn(@Param("tickets") List<Ticket> tickets);
}
