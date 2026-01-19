package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "companies")
@Audited
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Companies extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @Column(nullable = false, unique = true)
    private String companyName;

    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted = true;
}
