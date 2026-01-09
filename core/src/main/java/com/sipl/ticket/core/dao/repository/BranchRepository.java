package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Branches;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branches, Integer> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndBranchIdNot(String email, Integer branchId);

    @Query(
            "SELECT b " +
                    "FROM Branches b " +
                    "WHERE ( :isActive IS NULL OR b.isActive = :isActive ) " +
                    "AND ( :search IS NULL OR :search = '' " +
                    "   OR CAST(b.branchId AS string) LIKE CONCAT('%', :search, '%') " +
                    "   OR LOWER(b.branchName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR LOWER(b.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR LOWER(b.address) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR LOWER(b.company.companyName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR LOWER(b.country.countryName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR LOWER(b.state.stateName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR LOWER(b.city.cityName) LIKE LOWER(CONCAT('%', :search, '%')) )"
    )
    Page<Branches> searchBranches(
            @Param("search") String search,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );

}
