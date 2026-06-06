package com.ensf.fnf.core.dao.entity;

import com.ensf.fnf.core.enums.Gender;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String mobileNumber;

    private boolean verified;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    private LocalDate dateOfBirth;

    private Boolean married;

    private String spouseName;

    private LocalDate spouseDob;

    private LocalDate anniversaryDate;

    /**
     * Returns true when the user has completed their profile
     * (gender and DOB are set after the post-login profile setup step).
     */
    public boolean isProfileComplete() {
        return gender != null && dateOfBirth != null;
    }
}
