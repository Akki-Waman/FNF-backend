package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TicketResolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketResolutionRepository
        extends JpaRepository<TicketResolution, Long> {

    @Query("FROM TicketResolution tr WHERE tr.ticketResolutionId = :resolutionId")
    Optional<TicketResolution> findByIdWithAllDetails(
            @Param("resolutionId") Long resolutionId
    );

}
