package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.City;
import com.sipl.ticket.core.dto.response.CitySearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    boolean existsByCityNameIgnoreCaseAndState_StateId(
            String cityName, Long stateId
    );

    boolean existsByCityNameIgnoreCaseAndState_StateIdAndCityIdNot(
            String cityName, Long stateId, Long cityId
    );

    List<City> findByState_StateIdAndIsActiveTrue(Long stateId);

    @Query(
            "SELECT new com.sipl.ticket.core.dto.response.CitySearchResponseDto( " +
                    "   c.cityId, " +
                    "   c.cityName, " +
                    "   s.stateId, " +
                    "   s.stateName, " +
                    "   s.country.countryId, " +
                    "   s.country.countryName, " +
                    "   c.isActive " +
                    ") " +
                    "FROM City c " +
                    "JOIN c.state s " +
                    "JOIN s.country co " +
                    "WHERE c.isDeleted = false " +
                    "AND ( :isActive IS NULL OR c.isActive = :isActive ) " +
                    "AND ( :search IS NULL OR :search = '' " +
                    "   OR LOWER(c.cityName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR LOWER(s.stateName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR LOWER(co.countryName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    ")"
    )
    Page<CitySearchResponseDto> searchCities(
            @Param("search") String search,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );


}

