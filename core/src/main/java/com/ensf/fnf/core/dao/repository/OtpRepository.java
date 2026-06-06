package com.ensf.fnf.core.dao.repository;

import com.ensf.fnf.core.dao.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository
        extends JpaRepository<OtpEntity, Long> {

    @Query(
            "SELECT o " +
                    "FROM OtpEntity o " +
                    "WHERE o.mobileNumber = :mobileNumber " +
                    "ORDER BY o.id DESC"
    )
    Optional<OtpEntity> findLatestOtpByMobileNumber(
            @Param("mobileNumber")
            String mobileNumber
    );

    @Query(
            "SELECT o " +
                    "FROM OtpEntity o " +
                    "WHERE o.email = :email " +
                    "ORDER BY o.id DESC"
    )
    Optional<OtpEntity> findLatestOtpByEmail(
            @Param("email")
            String email
    );

    @Query(
            "SELECT o " +
                    "FROM OtpEntity o " +
                    "WHERE o.mobileNumber = :mobileNumber " +
                    "AND o.verified = false " +
                    "ORDER BY o.id DESC"
    )
    Optional<OtpEntity> findLatestUnverifiedOtpByMobileNumber(
            @Param("mobileNumber")
            String mobileNumber
    );

    @Query(
            "SELECT o " +
                    "FROM OtpEntity o " +
                    "WHERE o.email = :email " +
                    "AND o.verified = false " +
                    "ORDER BY o.id DESC"
    )
    Optional<OtpEntity> findLatestUnverifiedOtpByEmail(
            @Param("email")
            String email
    );

    @Query(
            "SELECT o " +
                    "FROM OtpEntity o " +
                    "WHERE o.mobileNumber = :mobileNumber " +
                    "AND o.otp = :otp " +
                    "AND o.verified = false"
    )
    Optional<OtpEntity> validateOtp(
            @Param("mobileNumber")
            String mobileNumber,
            @Param("otp")
            String otp
    );
}