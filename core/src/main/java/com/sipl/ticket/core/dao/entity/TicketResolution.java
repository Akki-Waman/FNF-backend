package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Audited
@Table(name = "ticket_resolution")
public class TicketResolution extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_resolution_id")
    private Long ticketResolutionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(name = "resolution_body", nullable = false)
    private String resolutionBody;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "status_before", length = 30)
    private String statusBefore;

    @Column(name = "status_after", length = 30)
    private String statusAfter;
}
