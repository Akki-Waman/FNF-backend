package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Origins;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OriginsRepository extends JpaRepository<Origins, Long> {

    @Query(
            "SELECT o " +
                    "FROM Origins o " +
                    "WHERE o.isActive = true " +
                    "AND LOWER(o.originName) = LOWER(:name)"
    )
    List<Origins> findActiveByOriginName(
            @Param("name") String name
    );

    @Query(
            "SELECT o " +
                    "FROM Origins o " +
                    "WHERE o.isActive = true " +
                    "AND (:originId IS NULL OR o.originId = :originId) " +
                    "ORDER BY o.originId DESC"
    )
    Page<Origins> searchByOriginId(
            @Param("originId") Long originId,
            org.springframework.data.domain.Pageable pageable
    );
}


