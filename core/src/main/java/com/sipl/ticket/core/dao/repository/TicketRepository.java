package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @EntityGraph(attributePaths = {
            "assignedTo",
            "location",
            "department",
            "clientProducts",
            "branch",
            "service",
            "contact"
    })
    @Query("FROM Ticket t WHERE t.ticketId = :ticketId")
    Optional<Ticket> findByIdWithAllDetails(@Param("ticketId") Long ticketId);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Ticket t SET t.isDeleted = true WHERE t.ticketId IN (:ids)")
    int softDeleteByIds(@Param("ids") List<Long> ids);

    @Query(
            "SELECT t FROM Ticket t " +
                    "WHERE t.isDeleted = false " +
                    "AND ( :query IS NULL OR :query = '' " +
                    "      OR t.searchText LIKE CONCAT('%', LOWER(:query), '%') )"
    )
    Page<Ticket> searchTickets(@Param("query") String query, Pageable pageable);



    @Query(
            "SELECT sm.valueDesc, COUNT(t.id) " +
                    "FROM Ticket t " +
                    "JOIN Masters sm ON sm.columnValue = t.status " +
                    "WHERE sm.columnCode = 2 " +
                    "GROUP BY sm.valueDesc, sm.columnValue " +
                    "ORDER BY sm.columnValue"
    )
    List<Object[]> countTicketsByStatus();

    @Query("SELECT t.ticketId FROM Ticket t WHERE t.isDeleted = false")
    List<Long> findAllActiveTicketIds();
}
