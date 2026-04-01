package com.sipl.ticket.core.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "sla_penalty_cap")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class SlaPenaltyCap extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer slaPenaltyCapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sla_profile_id", nullable = false)
    @JsonIgnore
    private SlaProfile slaProfile;

    @Column(nullable = false)
    private Double maxPenaltyPercent;

    @Column(nullable = false, length = 255)
    private String actionOnExceed;

    private Boolean isActive;
}
