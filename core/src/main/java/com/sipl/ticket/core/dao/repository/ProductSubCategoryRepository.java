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

    @Query(
            "SELECT CASE WHEN COUNT(psc) > 0 THEN true ELSE false END " +
                    "FROM ProductSubCategories psc " +
                    "WHERE LOWER(psc.productSubCategoryName) = LOWER(:name) " +
                    "AND psc.isActive = true " +
                    "AND psc.isDeleted = false"
    )
    boolean existsActiveByName(
            @Param("name") String name
    );

    boolean existsByProductSubCategoryNameIgnoreCaseAndProductSubCategoryIdNot(
            @Param("name") String name, @Param("productSubCategoryId") Long productSubCategoryId
    );

    // optional & clean
    List<ProductSubCategories> findByIsActiveTrue();

    @Query(
            "SELECT psc " +
                    "FROM ProductSubCategories psc " +
                    "WHERE psc.isDeleted = false " +
                    "AND ( :isActive IS NULL OR psc.isActive = :isActive ) " +
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

