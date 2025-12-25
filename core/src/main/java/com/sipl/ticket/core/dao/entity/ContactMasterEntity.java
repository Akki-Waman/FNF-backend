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
@Table(name = "ContactMaster")
public class ContactMasterEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ContactId")
    private Long contactId;

    @Column(name = "ContactName", nullable = false, length = 150)
    private String contactName;

    @Column(name = "EmailAddress", length = 150)
    private String emailAddress;

    @Column(name = "MobileNo", length = 15)
    private String mobileNo;

    @Column(name = "Department", length = 100)
    private String department;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    @Embedded
    private AuditEntity auditEntity;
}
