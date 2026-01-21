package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TicketNote;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketNoteRepository extends JpaRepository<TicketNote, Long> {
}
