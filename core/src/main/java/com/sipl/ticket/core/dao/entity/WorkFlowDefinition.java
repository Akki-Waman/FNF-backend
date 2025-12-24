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

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "entity_type", nullable = false, length = 100)
    private String entityType;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
