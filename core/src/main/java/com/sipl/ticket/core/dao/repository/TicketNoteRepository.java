package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TicketAttachment;
import com.sipl.ticket.core.dao.entity.TicketNote;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketNoteRepository extends JpaRepository<TicketNote, Long> {
    @Query("From TicketNote t WHERE t.ticket.ticketId= :ticketId")
    List<TicketNote> findByTicketId(@Param("ticketId")  Long ticketId);
}

