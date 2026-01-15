package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class TicketResponse extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_response_id")
    private Long ticketResponseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(name = "response_body", nullable = false)
    private String responseBody;

    @Column(name = "response_type", nullable = false, length = 20)
    private String responseType;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "status_before", length = 30)
    private String statusBefore;

    @Column(name = "status_after", length = 30)
    private String statusAfter;

    /* ===== New SLA & Penalty Columns ===== */

    @Column(name = "sla_hours")
    private Double  slaHours;

    @Column(name = "penalty_allowed")
    private Boolean penaltyAllowed;

    @Column(name = "response_time_hours")
    private Double  responseTimeHours;

    @Column(name = "within_sla")
    private Boolean withinSla;

    @Column(name = "penalty_time")
    private Integer penaltyTime;

    @Column(name = "penalty_percentage")
    private BigDecimal penaltyPercentage;
}
