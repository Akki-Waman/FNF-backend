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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "department_id",          // FK column in contact table
            referencedColumnName = "department_id",
            foreignKey = @ForeignKey(name = "fk_contact_department")
    )
    private Department department;


    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

}
