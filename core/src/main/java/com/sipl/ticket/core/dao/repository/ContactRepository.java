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
                    "JOIN c.department d " +
                    "WHERE (:query IS NULL OR " +
                    "   LOWER(c.contactName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    "   LOWER(c.emailAddress) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    "   c.mobileNo LIKE CONCAT('%', :query, '%') OR " +
                    "   LOWER(d.departmentName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                    "   CONCAT('', c.contactId) = :query OR " +
                    "   CONCAT('', d.departmentId) = :query " +
                    ") " +
                    "AND c.isActive = true"
    )
    Page<Contact> searchContacts(
            @Param("query") String query,
            Pageable pageable
    );



}
