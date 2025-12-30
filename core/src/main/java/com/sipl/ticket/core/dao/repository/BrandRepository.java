package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Brands;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    Optional<Brands> findByBrandId(Long brandId);

    @Query("SELECT b " +
            "FROM Brands b " +
            "WHERE b.isActive = true " +
            "AND (:brandId IS NULL OR b.brandId = :brandId)")
    Page<Brands> searchByBrandId(
            @Param("brandId") Long brandId,
            Pageable pageable
    );

}
