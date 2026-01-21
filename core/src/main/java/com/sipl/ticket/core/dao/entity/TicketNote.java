package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "ticket_notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class TicketNote extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_note_id")
    private Long ticketNoteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(name = "notes")
    private String notes;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
