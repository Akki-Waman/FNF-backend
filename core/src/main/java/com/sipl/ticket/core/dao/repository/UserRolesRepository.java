package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Roles;
import com.sipl.ticket.core.dao.entity.UserRoles;
import com.sipl.ticket.core.dao.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {

    @Query("SELECT r FROM Roles r " +
            "JOIN UserRoles urm ON urm.role.id = r.id " +
            "WHERE urm.user.id = :userId")
    List<Roles> findRolesByUserId(@Param("userId") Long userId);

    @Query("SELECT urm.user.id FROM UserRoles urm WHERE urm.role.id = :roleId")
    List<Long> findUserIdsByRoleId(Long roleId);

    @Query("SELECT u FROM UserRoles urm JOIN urm.user u " +
            "WHERE urm.role.id = :roleId AND urm.isActive = true AND u.isActive = true")
    List<Users> findActiveUsersByRoleId(@Param("roleId") Integer roleId);

    @Query("SELECT r FROM Roles r " +
            "JOIN UserRoles urm ON urm.role.id = r.id " +
            "WHERE urm.user.id = :userId AND urm.isActive = true")
    Roles findActiveRoleByUserId(@Param("userId") Long userId);

    @Query("SELECT urm FROM UserRoles urm WHERE urm.user.id = :userId")
    UserRoles findSingleByUserId(@Param("userId") Long userId);
}
