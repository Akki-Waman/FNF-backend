package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(
            "FROM Client c " +
                    "WHERE c.isDelete = false " +
                    "AND (:clientCode IS NULL OR LOWER(c.clientCode) LIKE LOWER(CONCAT('%', :clientCode, '%'))) " +
                    "AND (:clientName IS NULL OR LOWER(c.clientName) LIKE LOWER(CONCAT('%', :clientName, '%')))"
    )
    List<Client> searchClients(
            @Param("clientCode") String clientCode,
            @Param("clientName") String clientName
    );

}

