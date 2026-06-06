package com.ensf.fnf.core.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_otps")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    private String otp;

    @Column(name = "expiry_time")
    private LocalDateTime expiryTime;

    private boolean verified;

    @Column(name = "attempt_count")
    private Integer attemptCount;
}