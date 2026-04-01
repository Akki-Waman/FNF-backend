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

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END "+
    "FROM Country c "+
    "WHERE LOWER(c.countryName) = LOWER(:countryName) "+
      "AND c.countryId <> :countryId "+
      "AND c.isDeleted = false ")
    boolean existsByCountryNameIgnoreCaseAndCountryIdNot(
            @Param("countryName") String countryName,
            @Param("countryId") Long countryId
    );

    @Query("SELECT c FROM Country c WHERE c.isActive = true ORDER BY c.countryId DESC")
    Page<Country> findAllActive(Pageable pageable);


    @Query("SELECT c FROM Country c WHERE c.countryId = :id AND c.isActive = true")
    Country findActiveById(@Param("id") Long id);


    @Query(
            "SELECT c " +
                    "FROM Country c " +
                    "WHERE c.isDeleted = false " +
                    "AND ( :isActive IS NULL OR c.isActive = :isActive ) " +
                    "AND ( :search IS NULL OR :search = '' " +
                    "   OR LOWER(c.countryName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR LOWER(c.taxType) LIKE LOWER(CONCAT('%', :search, '%')) ) "
    )
    Page<Country> searchCountries(
            @Param("search") String search,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );

    @Query("SELECT COUNT(c) FROM Country c " +
            "WHERE LOWER(c.countryName) = LOWER(:name) " +
            "AND c.isActive = true " +
            "AND c.isDeleted = false")
    long countActiveCountry(@Param("name") String name);




}
