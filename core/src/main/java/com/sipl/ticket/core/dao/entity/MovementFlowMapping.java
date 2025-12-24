package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movement_flow_mapping")
public class MovementFlowMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movementFlowMappingId;

    @ManyToOne
    @JoinColumn(name = "movement_fk", referencedColumnName = "movement_id")
    private Movements movement;

    @ManyToOne
    @JoinColumn(name = "process_flow_fk", referencedColumnName = "process_flow_id")
    private ProcessFlow processFlow;

    private Integer sequence;

    private Boolean isActive;

    private Boolean isOptional;

    private Boolean isSapPosting;

    private Boolean isTripComplete;

    @ManyToOne
    @JoinColumn(name = "plant_id", referencedColumnName = "plant_id")
    private PlantMaster plantMaster;

    private Boolean isDecisionPoint;
}
