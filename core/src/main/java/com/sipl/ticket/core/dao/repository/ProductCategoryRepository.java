package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.ProductCategories;
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
public interface ProductCategoryRepository
        extends JpaRepository<ProductCategories, Long> {

    boolean existsByProductCategoryNameIgnoreCase(String name);

    boolean existsByProductCategoryNameIgnoreCaseAndProductCategoryIdNot(
            String name, Long productCategoryId
    );


    List<ProductCategories> findByIsActiveTrue();
    @Query(
            "SELECT pc " +
                    "FROM ProductCategories pc " +
                    "WHERE ( :isActive IS NULL OR pc.isActive = :isActive ) " +
                    "AND ( :search IS NULL " +
                    "   OR LOWER(pc.productCategoryName) LIKE LOWER(CONCAT('%', :search, '%')) )"
    )
    Page<ProductCategories> searchProductCategories(
            @Param("search") String search,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );



    Optional<ProductCategories> findByProductCategoryNameIgnoreCaseAndIsActive(String name, boolean b);
}

