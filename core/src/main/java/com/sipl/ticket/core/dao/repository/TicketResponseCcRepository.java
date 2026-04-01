package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TicketResponse;
import com.sipl.ticket.core.dao.entity.TicketResponseCc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketResponseCcRepository extends JpaRepository<TicketResponseCc, Long> {


    @Query(" SELECT c.email FROM TicketResponseCc c WHERE c.ticketResponse.ticketResponseId = :ticketResponseId")
    List<String> findCcEmailsByTicketResponseId(@Param("ticketResponseId")Long ticketResponseId);
}
