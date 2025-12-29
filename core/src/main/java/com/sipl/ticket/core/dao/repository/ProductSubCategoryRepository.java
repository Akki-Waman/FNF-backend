package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.ProductSubCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSubCategoryRepository
        extends JpaRepository<ProductSubCategories, Long> {

    boolean existsByProductSubCategoryNameIgnoreCaseAndIsDeletedFalse(
            String productSubCategoryName
    );

    boolean existsByProductSubCategoryNameIgnoreCaseAndProductSubCategoryIdNotAndIsDeletedFalse(
            String productSubCategoryName,
            Long productSubCategoryId
    );

    List<ProductSubCategories> findByIsDeletedFalse();

    List<ProductSubCategories> findByProductCategories_ProductCategoryIdAndIsDeletedFalse(
            Long productCategoryId
    );
}
