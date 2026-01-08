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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "severity_id", nullable = false)
    @JsonIgnore
    private Severity severity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sla_type_id", nullable = false)
    @JsonIgnore
    private SlaType slaType;

    @Column(nullable = false)
    private Integer slaHours;

    @Column(nullable = false)
    private Integer graceHours;

    @Column(nullable = false)
    private Double penaltyPercent;

    private Boolean isActive;
}
