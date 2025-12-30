package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Companies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Companies, Long> {

    boolean existsByCompanyNameIgnoreCase(String companyName);

    boolean existsByCompanyNameIgnoreCaseAndCompanyIdNot(
            String companyName, Long companyId
    );
}
