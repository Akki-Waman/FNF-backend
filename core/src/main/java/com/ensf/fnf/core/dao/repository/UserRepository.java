package com.ensf.fnf.core.dao.repository;

import com.ensf.fnf.core.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<
        UserEntity,
        Long> {

    @Query(
            "SELECT u " +
                    "FROM UserEntity u " +
                    "WHERE u.emailAddress = :email"
    )
    Optional<UserEntity>
    findByEmailAddress(
            @Param("email")
            String email
    );

    @Query(
            "SELECT u " +
                    "FROM UserEntity u " +
                    "WHERE u.mobileNumber = :mobileNumber"
    )
    Optional<UserEntity>
    findByMobileNumber(
            @Param("mobileNumber")
            String mobileNumber
    );

    @Query(
            "SELECT u " +
                    "FROM UserEntity u " +
                    "WHERE u.emailAddress = :username " +
                    "OR u.mobileNumber = :username"
    )
    Optional<UserEntity> findByUsername(
            @Param("username")
            String username
    );
}