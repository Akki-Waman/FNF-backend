package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.ProductSubCategories;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSubCategoryRepository
        extends JpaRepository<ProductSubCategories, Long> {

    boolean existsByProductSubCategoryNameIgnoreCase(String name);

    boolean existsByProductSubCategoryNameIgnoreCaseAndProductSubCategoryIdNot(
            String name, Long productSubCategoryId
    );

    // optional & clean
    List<ProductSubCategories> findByIsActiveTrue();

    @Query(
            "SELECT psc " +
                    "FROM ProductSubCategories psc " +
                    "WHERE psc.isActive = true " +
                    "AND (:productSubCategoryId IS NULL OR psc.productSubCategoryId = :productSubCategoryId) " +
                    "ORDER BY psc.productSubCategoryId DESC"
    )
    Page<ProductSubCategories> searchByProductSubCategoryId(
            @Param("productSubCategoryId") Long productSubCategoryId,
            Pageable pageable
    );
}

