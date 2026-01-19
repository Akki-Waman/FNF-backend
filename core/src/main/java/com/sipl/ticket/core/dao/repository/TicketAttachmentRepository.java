package com.sipl.ticket.core.dao.repository;

import com.opencsv.bean.CsvToBean;
import com.sipl.ticket.core.dao.entity.TicketAttachment;
import com.sipl.ticket.core.dao.entity.TicketAttachmentResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketAttachmentRepository extends JpaRepository<TicketAttachment, Long> {

    @Query("From TicketAttachment t WHERE t.ticket.ticketId= :ticketId")
    List<TicketAttachment> findByTicketId(@Param("ticketId") Long ticketId);
}
