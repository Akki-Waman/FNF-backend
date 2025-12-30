package com.sipl.ticket.core.dao.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gst_slab_master")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class GstSlabMaster extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slab_id")
    private Long slabId;

    @Column(name = "description")
    private String description;

    @Column(name = "cgst_rate")
    private BigDecimal cgstRate;

    @Column(name = "sgst_rate")
    private BigDecimal sgstRate;

    @Column(name = "igst_rate")
    private BigDecimal igstRate;

    @Column(name = "cess_rate")
    private BigDecimal cessRate;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "is_active")
    private Boolean isActive;
}
