package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TicketResolutionAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketResolutionAttachmentRepository
        extends JpaRepository<TicketResolutionAttachment, Long> {
}
