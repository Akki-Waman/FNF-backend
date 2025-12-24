package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.FaqCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqCategoryRepository extends JpaRepository<FaqCategory, Integer> {

    @Query(
            "FROM FaqCategory f WHERE "
                    + "f.isActive = true AND "
                    + "(:categoryName IS NULL OR LOWER(f.categoryName) LIKE LOWER(CONCAT('%', :categoryName, '%'))) AND "
                    + "(:categoryId IS NULL OR f.faqCategoryId = :categoryId)")
    Page<FaqCategory> findBySearchQuery(
            @Param("categoryName") String categoryName,
            @Param("categoryId") Integer categoryId,
            Pageable pageable);
}