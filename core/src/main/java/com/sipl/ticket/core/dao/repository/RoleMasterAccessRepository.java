package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.RoleMasterAccess;
import com.sipl.ticket.core.dao.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMasterAccessRepository extends JpaRepository<RoleMasterAccess, Long> {

    @Query("SELECT rma FROM RoleMasterAccess rma " +
            "WHERE rma.role = :role")
    List<RoleMasterAccess> findByRole(@Param("role") Roles role);

    @Query("SELECT CASE WHEN COUNT(rma) > 0 THEN true ELSE false END " +
            "FROM RoleMasterAccess rma " +
            "WHERE rma.role.id = :roleId AND rma.masterScreen.masterId = :masterId")
    boolean hasTransporterAccess(@Param("roleId") Long roleId, @Param("masterId") Long masterId);

}
