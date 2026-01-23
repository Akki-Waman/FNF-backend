package com.sipl.ticket.core.dao.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wfap_workflow_definition")
public class WorkFlowDefinition extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_flow_definition_id")
    private Integer workFlowDefinitionId;

    @Column(nullable = false)
    private String name;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
