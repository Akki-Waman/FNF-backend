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

    @Query(" SELECT b " +
           " FROM Branches b " +
           " WHERE (:isActive IS NULL OR b.isActive = :isActive) "+
           " AND (:companyId IS NULL OR b.company.companyId = :companyId) "+
             " AND (:branchId IS NULL OR b.branchId = :branchId) ")
    Page<Branches> searchBranches(
            @Param("branchId") Integer branchId,
            @Param("companyId") Long companyId,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );
}
