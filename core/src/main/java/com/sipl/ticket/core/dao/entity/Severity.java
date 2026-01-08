package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "severity_masters")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Severity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer severityId;

    @Column(nullable = false, unique = true)
    private String severityName;

    private Boolean isActive;
}
