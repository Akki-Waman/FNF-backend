package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Brands;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brands, Long> {

    boolean existsByBrandNameIgnoreCase(String brandName);

    boolean existsByBrandNameIgnoreCaseAndBrandIdNot(
            String brandName, Long brandId
    );

    List<Brands> findByIsActiveTrue();

    @Query("From Brands b where b.brandId = :brandId")
    Optional<Brands> findByBrandId(@Param("brandId") Long brandId);
}
