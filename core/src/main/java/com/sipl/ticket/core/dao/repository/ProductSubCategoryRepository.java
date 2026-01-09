package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.ProductSubCategories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
                    "WHERE ( :isActive IS NULL OR psc.isActive = :isActive ) " +
                    "AND ( :search IS NULL OR :search = '' " +
                    "   OR LOWER(psc.productSubCategoryName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR LOWER(psc.productCategories.productCategoryName) LIKE LOWER(CONCAT('%', :search, '%')) )"
    )
    Page<ProductSubCategories> searchProductSubCategories(
            @Param("search") String search,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );

}

