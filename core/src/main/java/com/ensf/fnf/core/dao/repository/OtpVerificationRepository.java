package com.ensf.fnf.core.dao.repository;

import com.ensf.fnf.core.dao.entity.OtpVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtpVerificationRepository
        extends JpaRepository<
        OtpVerificationEntity,
        Long> {

    @Query(
            "SELECT o " +
                    "FROM OtpVerificationEntity o " +
                    "WHERE o.username = :username " +
                    "ORDER BY o.createdDate DESC"
    )
    List<OtpVerificationEntity>
    findLatestOtpList(
            @Param("username")
            String username
    );

    @Query(
            "SELECT o " +
                    "FROM OtpVerificationEntity o " +
                    "WHERE o.username = :username " +
                    "AND o.verified = false " +
                    "ORDER BY o.createdDate DESC"
    )
    List<OtpVerificationEntity>
    findActiveOtpList(
            @Param("username")
            String username
    );
}
