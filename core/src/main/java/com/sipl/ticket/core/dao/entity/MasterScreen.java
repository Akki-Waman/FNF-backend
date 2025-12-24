package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rbac_master_screen")
public class MasterScreen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "master_id")
    private Long masterId;

    @Column(name = "master_name", nullable = false, unique = true)
    private String masterName;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "pk_column")
    private String pkColumn;

    @Column(name = "display_column")
    private String displayColumn;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Embedded
    private AuditEntity auditEntity;

}
