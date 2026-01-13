package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Masters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MastersRepository extends JpaRepository<Masters, Long> {

    List<Masters> findByColumnCode(Integer columnCode);
    @Query(
            "SELECT m.columnValue, m.valueDesc " +
                    "FROM Masters m " +
                    "WHERE m.tblName = :tblName " +
                    "AND m.columnCode = :columnCode " +
                    "AND m.isActive = true " +
                    "ORDER BY m.sequence"
    )
    List<Object[]> findActiveMasters(
            @Param("tblName") String tblName,
            @Param("columnCode") Integer columnCode
    );

    @Query("From Masters m where m.columnCode=?1 and m.columnValue=?2")
    Masters findByColumnCodeAndColumnValue(Integer columnCode, Integer status);
}
