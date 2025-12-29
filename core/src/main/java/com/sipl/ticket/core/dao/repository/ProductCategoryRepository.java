package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.ProductCategories;
import org.springframework.data.jpa.repository.JpaRepository;
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

    Optional<ProductCategories> findByProductCategoryId(Long productCategoryId);
}

