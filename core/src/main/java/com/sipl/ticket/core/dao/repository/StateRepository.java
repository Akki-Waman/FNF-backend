package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    boolean existsByStateNameIgnoreCase(String stateName);
    @Query(
            "SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
                    "FROM State s " +
                    "WHERE LOWER(s.stateName) = LOWER(:stateName) " +
                    "AND s.stateId <> :stateId"
    )
    boolean existsByStateNameIgnoreCaseAndStateIdNot(
            @Param("stateName") String stateName,
            @Param("stateId") Long stateId
    );


    List<State> findByIsActiveTrue();

    @Query(
            "SELECT s " +
                    "FROM State s " +
                    "WHERE s.isDeleted = false " +
                    "AND ( :isActive IS NULL OR s.isActive = :isActive ) " +
                    "AND ( :search IS NULL OR :search = '' " +
                    "   OR LOWER(s.stateName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "   OR LOWER(s.country.countryName) LIKE LOWER(CONCAT('%', :search, '%')) )"
    )
    Page<State> searchStates(
            @Param("search") String search,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );


}
