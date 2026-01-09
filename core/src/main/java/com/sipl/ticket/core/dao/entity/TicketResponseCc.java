package com.sipl.ticket.core.dao.entity;

import com.sipl.ticket.core.dto.response.AuditDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "ticket_response_cc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class TicketResponseCc extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_response_cc_id")
    private Long ticketResponseCcId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_response_id", nullable = false)
    private TicketResponse ticketResponse;

    @Column(name = "email", nullable = false, length = 150)
    private String email;
}
