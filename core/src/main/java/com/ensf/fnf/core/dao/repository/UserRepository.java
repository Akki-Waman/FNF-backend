package com.ensf.fnf.core.dao.repository;

import com.ensf.fnf.core.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(
            "SELECT CASE WHEN COUNT(u) > 0 " +
                    "THEN TRUE ELSE FALSE END " +
                    "FROM UserEntity u " +
                    "WHERE u.mobileNumber = :mobileNumber"
    )
    boolean existsByMobileNumber(
            @Param("mobileNumber")
            String mobileNumber
    );

    @Query(
            "SELECT CASE WHEN COUNT(u) > 0 " +
                    "THEN TRUE ELSE FALSE END " +
                    "FROM UserEntity u " +
                    "WHERE u.email = :email"
    )
    boolean existsByEmail(
            @Param("email")
            String email
    );

    @Query(
            "SELECT u FROM UserEntity u " +
                    "WHERE u.email = :email"
    )
    Optional<UserEntity> findByEmail(
            @Param("email")
            String email
    );

    @Query(
            "SELECT u FROM UserEntity u " +
                    "WHERE u.mobileNumber = :mobileNumber"
    )
    Optional<UserEntity> findByMobileNumber(
            @Param("mobileNumber")
            String mobileNumber
    );
}