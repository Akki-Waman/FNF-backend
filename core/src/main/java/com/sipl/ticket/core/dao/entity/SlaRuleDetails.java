package com.sipl.ticket.core.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "sla_rule_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class SlaRuleDetails extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer slaRuleDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sla_profile_id", nullable = false)
    @JsonIgnore
    private SlaProfile slaProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    @JsonIgnore
    private ServiceEntity service;

    @Column(name = "severity_master_id", nullable = false)
    private Integer severityMasterId;

    @Column(name = "sla_type_master_id", nullable = false)
    private Long slaTypeMasterId;

    @Column(nullable = false)
    private Integer slaHours;

    @Column(nullable = false)
    private Integer graceHours;

    @Column(nullable = false)
    private Double penaltyPercent;

    private Boolean isActive;
}
