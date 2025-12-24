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
@Table(name = "rbac_role_master_access")
public class RoleMasterAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;

    @ManyToOne
    @JoinColumn(name = "master_id", nullable = false)
    private MasterScreen masterScreen;

    @Column(name = "has_full_access", nullable = false)
    private Boolean hasFullAccess = false;

    @Embedded
    private AuditEntity auditEntity;
}

