package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Companies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Companies, Long> {

    boolean existsByCompanyNameIgnoreCase(String companyName);

    boolean existsByCompanyNameIgnoreCaseAndCompanyIdNot(
            String companyName, Long companyId
    );

    @Query(
            "SELECT c " +
                    "FROM Companies c " +
                    "WHERE ( :isActive IS NULL OR c.isActive = :isActive ) " +
                    "AND ( :search IS NULL OR :search = '' " +
                    "   OR LOWER(c.companyName) LIKE LOWER(CONCAT('%', :search, '%')) )"
    )
    Page<Companies> searchCompanies(
            @Param("search") String search,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );


}
