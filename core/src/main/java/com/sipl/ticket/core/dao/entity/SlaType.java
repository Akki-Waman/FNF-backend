package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "sla_type_masters")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class SlaType extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer slaTypeId;

    @Column(nullable = false, unique = true)
    private String slaTypeName;

    private Boolean isActive;
}
