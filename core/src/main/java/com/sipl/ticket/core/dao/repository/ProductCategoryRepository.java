package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.ProductCategories;
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
public interface ProductCategoryRepository
        extends JpaRepository<ProductCategories, Long> {

    boolean existsByProductCategoryNameIgnoreCase(String name);

    boolean existsByProductCategoryNameIgnoreCaseAndProductCategoryIdNot(
            String name, Long productCategoryId
    );

    // optional & clean
    List<ProductCategories> findByIsActiveTrue();
    @Query(
            "SELECT pc " +
                    "FROM ProductCategories pc " +
                    "WHERE pc.isActive = true " +
                    "AND (:productCategoryId IS NULL OR pc.productCategoryId = :productCategoryId) " +
                    "ORDER BY pc.productCategoryId DESC"
    )
    Page<ProductCategories> searchByProductCategoryId(
            @Param("productCategoryId") Long productCategoryId,
            Pageable pageable
    );

//    Page<ProductSubCategories> searchByProductSubCategoryId(Long productSubCategoryId, Pageable pageable);
}

