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
            "SELECT o FROM Origins o " +
                    "WHERE o.isDelete = false " +
                    "AND ( :isActive IS NULL OR o.isActive = :isActive ) " +
                    "AND ( :query IS NULL OR :query = '' " +
                    "      OR LOWER(o.originName) LIKE CONCAT('%', LOWER(:query), '%') )"
    )
    Page<Origins> searchOrigins(
            @Param("query") String query,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );


}


