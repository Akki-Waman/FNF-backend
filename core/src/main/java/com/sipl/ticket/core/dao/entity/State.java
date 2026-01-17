package com.sipl.ticket.core.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "states")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class State extends AuditEntity {

    private static final long serialVersionUID = -3023296542024322252L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "state_id")
    private Long stateId;

    @Column(name = "state_name", nullable = false, length = 100)
    private String stateName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "country_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_states_country_id")
    )
    private Country country;

    @Column(name = "is_active")
    private Boolean isActive;


    @Column(name = "is_deleted")
    private Boolean isDeleted = true;
}
