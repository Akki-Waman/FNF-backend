package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Branches;
import com.sipl.ticket.core.dao.entity.Brands;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branches, Integer> {

    boolean existsByEmailIgnoreCase(@Param("email") String email);

    boolean existsByEmailIgnoreCaseAndBranchIdNot(@Param("email") String email, @Param("branchId") Integer branchId);

    @Query(
            "SELECT b " +
                    "FROM Branches b " +
                    "WHERE b.isDeleted = false " +
                    "AND (:isActive IS NULL OR b.isActive = :isActive) " +

                    "AND ( " +
                    "   :branchId IS NULL " +
                    "   OR b.company.companyId = ( " +
                    "        SELECT b2.company.companyId " +
                    "        FROM Branches b2 " +
                    "        WHERE b2.branchId = :branchId " +
                    "   ) " +
                    ") " +

                    "AND ( " +
                    "   :search IS NULL OR :search = '' " +
                    "   OR LOWER(b.branchName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR LOWER(b.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR LOWER(b.address) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR LOWER(b.company.companyName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR LOWER(b.country.countryName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR LOWER(b.state.stateName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR LOWER(b.city.cityName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    ")"
    )
    Page<Branches> searchBranches(
            @Param("search") String search,
            @Param("branchId") Integer branchId,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );



    @Query("from Branches b where b.branchId = :branchId")
    Optional<Branches> findByBranchId(@Param("branchId") Integer branchId);


}
