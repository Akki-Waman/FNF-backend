package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
