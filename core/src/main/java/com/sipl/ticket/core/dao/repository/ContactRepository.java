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
                    "LEFT JOIN c.department d " +
                    "WHERE c.isDelete = false " +
                    "AND c.isActive = true " +
                    "AND (:contactId IS NULL OR c.contactId = :contactId) " +
                    "AND (:departmentId IS NULL OR d.departmentId = :departmentId) " +
                    "AND ( :query IS NULL " +
                    "   OR LOWER(c.contactName) LIKE LOWER(CONCAT('%', :query, '%')) " +
                    "   OR LOWER(c.emailAddress) LIKE LOWER(CONCAT('%', :query, '%')) " +
                    "   OR c.mobileNo LIKE CONCAT('%', :query, '%') " +
                    "   OR LOWER(d.departmentName) LIKE LOWER(CONCAT('%', :query, '%')) " +
                    "   OR CAST(c.contactId AS string) = :query " +
                    "   OR CAST(d.departmentId AS string) = :query " +
                    ")"
    )
    Page<Contact> searchContacts(
            @Param("contactId") Long contactId,
            @Param("departmentId") Long departmentId,
            @Param("query") String query,
            Pageable pageable
    );


}
