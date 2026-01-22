package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    /* ================= VALIDATION ================= */

    boolean existsByEmailAddressIgnoreCaseAndIsActiveTrue(String emailAddress);

    boolean existsByMobileNoAndIsActiveTrue(String mobileNo);

    boolean existsByEmailAddressIgnoreCaseAndContactIdNotAndIsActiveTrue(
            String emailAddress,
            Long contactId
    );

    boolean existsByMobileNoAndContactIdNotAndIsActiveTrue(
            String mobileNo,
            Long contactId
    );

    /* ================= FETCH ================= */

    List<Contact> findByIsActiveTrue();

    @Query(
            "SELECT c FROM Contact c " +
                    "JOIN c.branch b " +
                    "WHERE c.isDelete = false " +
                    "AND ( :isActive IS NULL OR c.isActive = :isActive ) " +
                    "AND ( :contactId IS NULL OR c.contactId = :contactId ) " +
                    "AND ( :branchId IS NULL OR b.branchId = :branchId ) " +
                    "AND ( :query IS NULL OR :query = '' " +
                    "   OR LOWER(c.contactName) LIKE CONCAT('%', LOWER(:query), '%') " +
                    "   OR LOWER(c.emailAddress) LIKE CONCAT('%', LOWER(:query), '%') " +
                    "   OR c.mobileNo LIKE CONCAT('%', :query, '%') " +
                    "   OR CAST(c.contactId AS string) LIKE CONCAT('%', :query, '%') " +
                    ")"
    )
    Page<Contact> searchContacts(
            @Param("contactId") Long contactId,
            @Param("branchId") Integer branchId,
            @Param("query") String query,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );



    @Query(
            "SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
                    "FROM Contact c " +
                    "WHERE LOWER(c.contactName) = LOWER(:contactName) " +
                    "AND c.branch.branchId = :branchId " +
                    "AND c.isDelete = false"
    )
    boolean existsActiveContactInDepartment(
            @Param("contactName") String contactName,
            @Param("branchId") Integer branchId
    );


}
