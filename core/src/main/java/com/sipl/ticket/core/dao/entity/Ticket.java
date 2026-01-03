package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tickets")
@Audited
public class Ticket extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "subject", nullable = false, length = 255)
    private String subject;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Contact contact;

    @Column(name = "complaint_name", length = 150)
    private String complaintName;

    @Column(name = "complaint_mobile_no", length = 15)
    private String complaintMobileNo;

    @Column(name = "email_address", length = 150)
    private String emailAddress;

    @ManyToOne
    @JoinColumn(name = "location_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Locations location;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "client_product_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private ClientProducts clientProducts;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @ManyToOne
    @JoinColumn(name = "service_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private ServiceEntity service;

    @ManyToOne(optional = false)
    @JoinColumn(name = "assigned_to")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Users assignedTo;

    @Column(name = "status", nullable = false)
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Branches branch;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;


    /* ---------------- Relationships ---------------- */

    @OneToMany(
            mappedBy = "ticket",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TicketTag> ticketTags = new ArrayList<>();

    @OneToMany(
            mappedBy = "ticket",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TicketCc> ticketCcs = new ArrayList<>();

    @OneToMany(
            mappedBy = "ticket",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TicketAttachment> attachments = new ArrayList<>();

}
