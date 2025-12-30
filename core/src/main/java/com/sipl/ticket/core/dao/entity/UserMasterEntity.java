package com.sipl.ticket.core.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "rbac_user_master")
public class UserMasterEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "first_name", length = 200)
    private String firstName;

    @Column(name = "middle_name", length = 200)
    private String middleName;

    @Column(name = "last_name",  length = 200)
    private String lastName;

    @Column(name = "user_name", unique = true, length = 200)
    private String userName;

    @Column(name = "password", length = 200)
    private String password;

    @Column(name = "phone_number", unique = true, length = 20)
    private String phoneNumber;

    @Column(name = "email", unique = true, length = 200)
    private String emailId;

    @Column(name = "dob", columnDefinition = "DATE")
    private LocalDate dob;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name="raw_password")
    private String rawPassword;


}