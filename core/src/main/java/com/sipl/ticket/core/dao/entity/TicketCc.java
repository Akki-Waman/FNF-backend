package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "ticket_cc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class TicketCc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_cc_id")
    private Long ticketCcId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column(name = "cc", nullable = false)
    private String cc;
}