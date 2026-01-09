package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

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

}
