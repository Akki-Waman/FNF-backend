package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Page<Users> findByIsActiveTrue(Pageable pageable);

    List<Users> findByIsActiveTrue();

    Page<Users> findByIsActiveTrueAndUserNameContainingIgnoreCase(String userName, Pageable pageable);

    @Query("FROM Users u WHERE LOWER(u.userName) = LOWER(:username)")
    Optional<Users> findByUserName(@Param("username") String username);

    @Query("FROM Users u WHERE LOWER(u.emailId) = LOWER(:emailId)")
    Optional<Users> findByEmailId(@Param("emailId") String emailId);

    @Query("SELECT u.emailId FROM Users u WHERE u.id IN :usersIds AND u.isActive = true")
    List<String> findActiveEmailsByUserIds(@Param("usersIds") List<Long> usersIds);

    @Query("FROM Users u WHERE LOWER(u.phoneNumber) = LOWER(:mobileNumber)")
    Optional<Users> findByMobileNumber(@Param("mobileNumber") String mobileNumber);
}
