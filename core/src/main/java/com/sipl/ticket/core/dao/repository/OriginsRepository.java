package com.sipl.ticket.core.dao.repository;
import com.sipl.ticket.core.dao.entity.Origins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OriginsRepository extends JpaRepository<Origins, Long> {

    @Query(
            "FROM Origins o " +
                    "WHERE (:isActive IS NULL OR o.isActive = :isActive) " +
                    "AND (:originName IS NULL OR LOWER(o.originName) LIKE LOWER(CONCAT('%', :originName, '%')))"
    )
    List<Origins> searchOrigins(
            @Param("originName") String originName,
            @Param("isActive") Boolean isActive
    );

}
