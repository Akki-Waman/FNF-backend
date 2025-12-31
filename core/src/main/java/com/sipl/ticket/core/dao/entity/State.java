package com.sipl.ticket.core.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

@Table(name = "states")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class State extends AuditEntity {

    private static final long serialVersionUID = -3023296542024322252L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stateId;

    private String stateName;

    @ManyToOne
    @JoinColumn(
            name = "country_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_states_country_id"))
    private Country country;

    private Boolean isActive;
}
