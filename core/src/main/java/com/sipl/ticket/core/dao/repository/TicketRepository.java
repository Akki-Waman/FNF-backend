package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Ticket;
import com.sipl.ticket.core.dto.response.ChartItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
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

    @Query(value =
            "SELECT * FROM tickets t " +
                    "WHERE t.is_deleted = 0 " +
                    "AND ( :branchId IS NULL OR t.branch_id = :branchId ) " +
                    "AND ( :status IS NULL OR t.status = :status ) " +
                    "AND ( :createdBy IS NULL OR t.created_by = :createdBy ) " +
                    "AND ( COALESCE(:companyIds, NULL) IS NULL " +
                    "       OR t.branch_id IN ( " +
                    "           SELECT b.branch_id FROM branches b " +
                    "           WHERE b.company_id IN (:companyIds) " +
                    "       ) ) " +
                    "AND ( :query IS NULL OR :query = '' " +
                    "       OR t.search_text LIKE '%' + :query + '%' )",
            countQuery =
                    "SELECT COUNT(*) FROM tickets t " +
                            "WHERE t.is_deleted = 0 " +
                            "AND ( :branchId IS NULL OR t.branch_id = :branchId ) " +
                            "AND ( :status IS NULL OR t.status = :status ) " +
                            "AND ( :createdBy IS NULL OR t.created_by = :createdBy ) " +
                            "AND ( COALESCE(:companyIds, NULL) IS NULL " +
                            "       OR t.branch_id IN ( " +
                            "           SELECT b.branch_id FROM branches b " +
                            "           WHERE b.company_id IN (:companyIds) " +
                            "       ) ) " +
                            "AND ( :query IS NULL OR :query = '' " +
                            "       OR t.search_text LIKE '%' + :query + '%' )",
            nativeQuery = true)
    Page<Ticket> searchTickets(
            @Param("query") String query,
            @Param("branchId") Integer branchId,
            @Param("status") Integer status,
            @Param("companyIds") List<Long> companyIds,
            @Param("createdBy") Long createdBy,
            Pageable pageable
    );

    @Query(
            "SELECT sm.valueDesc, COUNT(t.ticketId) " +
                    "FROM Ticket t " +
                    "JOIN Masters sm ON sm.columnValue = t.status " +
                    "WHERE sm.columnCode = 2 " +
                    "AND t.isDeleted = false " +
                    "AND sm.isActive = true " +
                    "GROUP BY sm.valueDesc, sm.columnValue " +
                    "ORDER BY sm.columnValue"
    )
    List<Object[]> countTicketsByStatus();


    @Query("SELECT t.ticketId FROM Ticket t WHERE t.isDeleted = false ORDER BY t.ticketId DESC")
    List<Long> findAllActiveTicketIds();

    @Query(
            "SELECT t " +
                    "FROM Ticket t " +
                    "LEFT JOIN t.clientProducts cp " +
                    "LEFT JOIN t.service s " +
                    "WHERE t.isDeleted = false " +
                    "AND ( " +
                    "   :query IS NULL OR :query = '' OR " +
                    "   CAST(t.ticketId AS string) LIKE CONCAT('%', :query, '%') OR " +
                    "   LOWER(cp.groupName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    "   LOWER(cp.deviceName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    "   LOWER(s.serviceName) LIKE LOWER(CONCAT('%', :query, '%')) " +
                    ")"
    )
    Page<Ticket> searchResponsePenaltyReport(
            @Param("query") String query,
            Pageable pageable
    );


    @Query("SELECT new com.sipl.ticket.core.dto.response.ChartItemDTO(sm.valueDesc, COALESCE(COUNT(t.ticketId), 0)) " +
            "FROM Masters sm " +
            "LEFT JOIN Ticket t ON t.status = sm.columnValue AND t.isDeleted = false " +
            "WHERE sm.columnCode = :columnId AND sm.isActive = true " +
            "GROUP BY sm.valueDesc, sm.columnValue " +
            "ORDER BY sm.columnValue")
    List<ChartItemDTO> getTicketsByStatus(@Param("columnId") Integer columnId);

    @Query("SELECT new com.sipl.ticket.core.dto.response.ChartItemDTO(sm.valueDesc, COALESCE(COUNT(t.ticketId), 0)) " +
            "FROM Masters sm " +
            "LEFT JOIN Ticket t ON t.priority = sm.columnValue AND t.isDeleted = false " +
            "WHERE sm.columnCode = :columnId AND sm.isActive = true " +
            "GROUP BY sm.valueDesc, sm.columnValue " +
            "ORDER BY sm.columnValue")
    List<ChartItemDTO> getTicketsByPriority(@Param("columnId") Integer columnId);

    @Query(
            "SELECT t FROM Ticket t " +
                    "WHERE t.isDeleted = false " +
                    "AND (:isActive IS NULL OR t.isDeleted = false) " +
                    "AND (:branchId IS NULL OR t.branch.branchId = :branchId) " +
                    "AND ( " +
                    "     :query IS NULL OR " +
                    "     t.searchText LIKE CONCAT('%', :query, '%') OR " +   // FIXED
                    "     LOWER(t.subject) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    "     LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    "     LOWER(t.assignedTo.userName) LIKE LOWER(CONCAT('%', :query, '%')) " +
                    ") " +
                    "AND (:fromDate IS NULL OR t.createdTime >= :fromDate) " +
                    "AND (:toDate IS NULL OR t.createdTime <= :toDate)"
    )
    List<Ticket> findTicketsForStaffReport(
            @Param("query") String query,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("branchId") Integer branchId,
            @Param("isActive") Boolean isActive
    );

    @Query(
            "SELECT new com.sipl.ticket.core.dto.response.ChartItemDTO(" +
                    "COALESCE(CONCAT(u.firstName, ' ', u.lastName), 'Unknown'), " +
                    "COUNT(t.ticketId)" +
                    ") " +
                    "FROM Ticket t " +
                    "LEFT JOIN t.assignedTo u " +
                    "WHERE t.isDeleted = false " +
                    "GROUP BY u.firstName, u.lastName " +
                    "ORDER BY COUNT(t.ticketId) DESC"
    )
    List<ChartItemDTO> getTicketsByAssignee();


    @Query("SELECT t FROM Ticket t WHERE t.status NOT IN :statuses ORDER BY t.ticketId DESC")
    List<Ticket> findByStatusNotIn(@Param("statuses") List<Integer> statuses);



}

