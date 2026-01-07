package com.sipl.ticket.core.dao.entity;

import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Client extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "client_code", nullable = false, length = 50)
    private String clientCode;

    @Column(name = "client_name", nullable = false, length = 150)
    private String clientName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}