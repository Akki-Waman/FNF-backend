package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plant_master")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class PlantMaster extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plant_id")
    private Long plantId;

    @Column(name = "plant_code", nullable = false, unique = true, length = 50)
    private String plantCode;

    @Column(name = "plant_description")
    private String plantDescription;

    @Column(name = "active")
    private Boolean active;
}
