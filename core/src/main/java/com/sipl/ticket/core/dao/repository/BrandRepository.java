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

    boolean existsByBrandNameIgnoreCase(@Param("brandName") String brandName);

    boolean existsByBrandNameIgnoreCaseAndBrandIdNot(
           @Param("brandName") String brandName,@Param("brandId") Long brandId
    );

    List<Brands> findByIsActiveTrue();

    @Query("From Brands b where b.brandId = :brandId")
    Optional<Brands> findByBrandId(@Param("brandId") Long brandId);

    @Query(
            "SELECT b FROM Brands b " +
                    "WHERE  ( :isActive IS NULL OR b.isActive = :isActive ) " +
                    "AND ( :query IS NULL OR :query = '' " +
                    "      OR LOWER(b.brandName) LIKE CONCAT('%', LOWER(:query), '%') )"
    )
    Page<Brands> searchBrands(
            @Param("query") String query,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );


}
