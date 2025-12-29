package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Brands;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brands, Long> {

    boolean existsByBrandNameIgnoreCaseAndIsDeletedFalse(String brandName);

    boolean existsByBrandNameIgnoreCaseAndBrandIdNotAndIsDeletedFalse(
            String brandName, Long brandId
    );

    List<Brands> findByIsDeletedFalse();
}
