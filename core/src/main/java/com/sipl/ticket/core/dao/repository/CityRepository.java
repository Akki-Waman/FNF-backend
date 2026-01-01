package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.City;
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

    @Query("SELECT c " +
            "FROM City c " +
            "WHERE c.isActive = true " +
            "AND (:cityId IS NULL OR c.cityId = :cityId) " +
            "AND (:stateId IS NULL OR c.state.stateId = :stateId) " +
            "AND (:cityName IS NULL OR LOWER(c.cityName) LIKE LOWER(CONCAT('%', :cityName, '%'))) ")
    Page<City> searchCities(
            @Param("cityId") Long cityId,
            @Param("stateId") Long stateId,
            @Param("cityName") String cityName,
            Pageable pageable
    );

}

