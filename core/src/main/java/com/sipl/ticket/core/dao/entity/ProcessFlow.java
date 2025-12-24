package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "process_flow")
public class ProcessFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "process_flow_id")
    private Long processFlowId;

    private String processFlowName;

    @ManyToOne
    @JoinColumn(name = "screen_master_fk", referencedColumnName = "screenMasterId")
    private ScreenMaster screenMaster;

    private String operationType;
}
