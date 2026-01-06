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
            "SELECT DISTINCT t FROM Ticket t " +

                    "LEFT JOIN t.department d " +
                    "LEFT JOIN t.branch b " +
                    "LEFT JOIN b.company c " +
                    "LEFT JOIN t.location l " +
                    "LEFT JOIN t.service s " +
                    "LEFT JOIN t.clientProducts cp " +
                    "LEFT JOIN t.assignedTo u " +

                    "WHERE ( " +
                    " :query IS NULL OR :query = '' OR " +
                    " STR(t.ticketId) LIKE CONCAT('%', :query, '%') OR " +
                    " LOWER(COALESCE(t.subject,'')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    " LOWER(COALESCE(t.description,'')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    " LOWER(COALESCE(t.complaintName,'')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    " COALESCE(t.complaintMobileNo,'') LIKE CONCAT('%', :query, '%') OR " +
                    " LOWER(COALESCE(t.emailAddress,'')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    " LOWER(COALESCE(d.departmentName,'')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    " STR(d.departmentId) LIKE CONCAT('%', :query, '%') OR " +
                    " LOWER(COALESCE(b.branchName,'')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    " LOWER(COALESCE(b.address,'')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    " LOWER(COALESCE(b.email,'')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    " LOWER(COALESCE(c.companyName,'')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    " STR(c.companyId) LIKE CONCAT('%', :query, '%') OR " +
                    " LOWER(COALESCE(l.locationName,'')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    " LOWER(COALESCE(s.serviceName,'')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    " LOWER(COALESCE(cp.groupName,'')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    " LOWER(COALESCE(cp.deviceName,'')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    " COALESCE(cp.serialNumber,'') LIKE CONCAT('%', :query, '%') OR " +
                    " COALESCE(cp.imeiNo,'') LIKE CONCAT('%', :query, '%') OR " +
                    " LOWER(COALESCE(u.firstName,'')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    " LOWER(COALESCE(u.lastName,'')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    " LOWER(COALESCE(u.userName,'')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    " LOWER(COALESCE(u.emailId,'')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    " STR(t.status) LIKE CONCAT('%', :query, '%') OR " +
                    " STR(t.priority) LIKE CONCAT('%', :query, '%') " +
                    ")"
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

}
