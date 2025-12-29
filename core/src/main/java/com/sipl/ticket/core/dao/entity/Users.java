package com.sipl.ticket.core.dao.entity;

import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rbac_user_master")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Users extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 200)
    private String firstName;

    @Column(name = "middle_name", length = 200)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 200)
    private String lastName;

    @Column(name = "user_name", nullable = false, unique = true, length = 200)
    private String userName;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Column(name = "phone_number", unique = true, length = 20)
    private String phoneNumber;

    @Column(name = "email", unique = true, length = 200)
    private String emailId;

    @Column(name = "dob", nullable = false, columnDefinition = "DATE")
    private LocalDate dob;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "raw_password")
    private String rawPassword;
}
