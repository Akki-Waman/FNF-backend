package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TicketAttachment;
import com.sipl.ticket.core.dao.entity.TicketNote;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketNoteRepository extends JpaRepository<TicketNote, Long> {
    @Query("From TicketNote t WHERE t.ticket.ticketId= :ticketId")
    List<TicketNote> findByTicketId(@Param("ticketId")  Long ticketId);

    @Query(
            "SELECT tn FROM TicketNote tn " +
                    "WHERE tn.ticket.ticketId = :ticketId " +
                    "AND tn.isDeleted = false"
    )
    Optional<TicketNote> findActiveByTicketId(@Param("ticketId") Long ticketId);

    @Query(" SELECT CASE WHEN COUNT(tn) > 0 THEN true ELSE false END "+
       "FROM TicketNote tn "+
       "WHERE tn.ticket.ticketId = :ticketId "+
       "AND tn.isDeleted = false ")
    boolean existsActiveByTicketId(@Param("ticketId") Long ticketId);

}

