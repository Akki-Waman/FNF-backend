package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TicketAttachment;
import com.sipl.ticket.core.dao.entity.TicketResponseAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  TicketResponseAttachmentRepository extends JpaRepository<TicketResponseAttachment,Long> {
}
