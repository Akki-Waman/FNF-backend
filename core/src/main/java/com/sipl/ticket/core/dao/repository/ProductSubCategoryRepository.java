package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.ProductSubCategories;
import org.springframework.data.jpa.repository.JpaRepository;
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

    Optional<ProductSubCategories> findByProductSubCategoryId(Long productSubCategoryId);
}

