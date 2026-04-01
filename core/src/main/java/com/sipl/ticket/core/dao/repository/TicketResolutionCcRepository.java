package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TicketResolutionCc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketResolutionCcRepository
        extends JpaRepository<TicketResolutionCc, Long> {

    @Query(
            "SELECT trc.email " +
                    "FROM TicketResolutionCc trc " +
                    "WHERE trc.ticketResolution.ticketResolutionId = :resolutionId"
    )
    List<String> findCcEmailsByTicketResolutionId(
            @Param("resolutionId") Long resolutionId
    );

}
