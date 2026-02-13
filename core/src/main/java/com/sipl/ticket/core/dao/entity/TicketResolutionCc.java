package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Audited
@Table(name = "ticket_resolution_cc")
public class TicketResolutionCc extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_resolution_cc_id")
    private Long ticketResolutionCcId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_resolution_id", nullable = false)
    private TicketResolution ticketResolution;

    @Column(name = "email", nullable = false, length = 150)
    private String email;
}
