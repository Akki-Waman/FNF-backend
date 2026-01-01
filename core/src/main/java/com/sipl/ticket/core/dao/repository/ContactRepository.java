package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Contact;
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

    /* ================= SEARCH ================= */

    /*@Query("SELECT c FROM Contact c"+
        "WHERE (:contactName IS NULL OR LOWER(c.contactName) LIKE LOWER(CONCAT('%', :contactName, '%')))"+
          "AND (:emailAddress IS NULL OR LOWER(c.emailAddress) LIKE LOWER(CONCAT('%', :emailAddress, '%')))"+
          "AND (:mobileNo IS NULL OR c.mobileNo LIKE CONCAT('%', :mobileNo, '%'))"+
          "AND (:departmentId IS NULL OR c.department.departmentId = :departmentId)"+
          "AND (:isActive IS NULL OR c.isActive = :isActive)"
    )
    List<Contact> searchContacts(
            @Param("contactName") String contactName,
            @Param("emailAddress") String emailAddress,
            @Param("mobileNo") String mobileNo,
            @Param("departmentId") Long departmentId,
            @Param("isActive") Boolean isActive
    ); */
}
