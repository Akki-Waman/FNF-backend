package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    boolean existsByCountryNameIgnoreCase(String name);

    boolean existsByCountryNameIgnoreCaseAndCountryIdNot(String name, Long countryId);


    @Query("SELECT c FROM Country c WHERE c.isActive = true ORDER BY c.countryId DESC")
    Page<Country> findAllActive(Pageable pageable);


    @Query("SELECT c FROM Country c WHERE c.countryId = :id AND c.isActive = true")
    Country findActiveById(@Param("id") Long id);


    @Query("SELECT c FROM Country c " +
            "WHERE (:countryId IS NULL OR c.countryId = :countryId) " +
            "AND (:countryName IS NULL OR LOWER(c.countryName) LIKE LOWER(CONCAT('%', :countryName, '%'))) " +
            "AND (:taxType IS NULL OR c.taxType = :taxType) " +
            "AND (:isForeign IS NULL OR c.isForeign = :isForeign) " +
            "AND (:isActive IS NULL OR c.isActive = :isActive) ")
    Page<Country> searchCountries(
            @Param("countryId") Long countryId,
            @Param("countryName") String countryName,
            @Param("taxType") String taxType,
            @Param("isForeign") Boolean isForeign,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );
}
