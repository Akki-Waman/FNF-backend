package com.sipl.ticket.core.dao.entity;


import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_stages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class TransactionStages extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_stage_id")
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "transaction_id", referencedColumnName = "transaction_id")
//    private Transactions transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movement_flow_mapping_id", referencedColumnName = "movementFlowMappingId")
    private MovementFlowMapping movementFlowMapping;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", referencedColumnName = "plant_id")
    private PlantMaster plant;

    @Column(name = "action_time")
    private LocalDateTime actionTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_by", referencedColumnName = "user_id")
    private Users actionBy;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "location_id", referencedColumnName = "location_id")
//    private Locations location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id", referencedColumnName = "master_id")
    private MasterScreen masterScreen;

    private Long entityId;

    @Column(name = "remarks", length = 200)
    private String remarks;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @Column(name = "is_active")
    private Boolean isActive;
}

