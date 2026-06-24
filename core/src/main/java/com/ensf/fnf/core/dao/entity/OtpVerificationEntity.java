package com.ensf.fnf.core.dao.entity;

import com.ensf.fnf.core.enums.OtpPurpose;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_verifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpVerificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_verification_id")
    private Long otpVerificationId;

    @Column(name = "username")
    private String username;

    @Column(name = "otp_code")
    private String otpCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "otp_purpose")
    private OtpPurpose otpPurpose;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "expiry_time")
    private LocalDateTime expiryTime;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;
}