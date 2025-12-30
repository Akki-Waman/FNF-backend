package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "operational_units")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class OperationalUnit extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "op_unit_id")
    private Long operationalUnitId;

    @Column(name = "op_unit_name", length = 150, nullable = false)
    private String operationalUnitName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id", nullable = false)
    private Divisions division;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
