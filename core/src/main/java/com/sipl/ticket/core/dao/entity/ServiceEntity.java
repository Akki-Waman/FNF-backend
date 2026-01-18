package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "services")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class ServiceEntity extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "service_name", length = 150, nullable = true)
    private String serviceName;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_delete",nullable = false)
    private Boolean isDelete = true;

}

