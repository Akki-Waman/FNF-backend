package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contact")
public class Contact extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long contactId;

    @Column(name = "contact_name", nullable = false, length = 150)
    private String contactName;

    @Column(name = "email_address", length = 150)
    private String emailAddress;

    @Column(name = "mobile_no", length = 15)
    private String mobileNo;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

}
