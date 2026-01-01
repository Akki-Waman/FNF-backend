package com.sipl.ticket.core.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

@Entity
@Data
@Table(name = "cities")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class City extends AuditEntity {

    private static final long serialVersionUID = -3746060998503013085L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cityId;

    private String cityName;

    @ManyToOne
    @JoinColumn(
            name = "state_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_cities_state_id"))
    private State state;

    private Boolean isActive;
}
