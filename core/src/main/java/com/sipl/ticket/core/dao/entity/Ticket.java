package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Column(name = "search_text", columnDefinition = "TEXT")
    private String searchText;

    /* ===== RESPONSE (REPLY) SLA ===== */

    @Column(name = "response_sla_hours")
    private Double responseSlaHours;

    @Column(name = "response_time_hours")
    private Double responseTimeHours;

    @Column(name = "response_within_sla")
    private Boolean responseWithinSla;

    @Column(name = "response_penalty_time")
    private Double responsePenaltyTime;

    @Column(name = "response_penalty_percentage")
    private BigDecimal responsePenaltyPercentage;

    @Column(name = "response_datetime")
    private LocalDateTime responseDateTime;

    /* ===== RESOLUTION (CLOSE) SLA ===== */

    @Column(name = "resolution_sla_hours")
    private Double resolutionSlaHours;

    @Column(name = "resolution_time_hours")
    private Double resolutionTimeHours;

    @Column(name = "resolution_within_sla")
    private Boolean resolutionWithinSla;

    @Column(name = "resolution_penalty_time")
    private Double resolutionPenaltyTime;

    @Column(name = "resolution_penalty_percentage")
    private BigDecimal resolutionPenaltyPercentage;

    @Column(name = "resolution_datetime")
    private LocalDateTime resolutionDateTime;

    @Column(name = "penalty_allowed")
    private Boolean penaltyAllowed;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @Column(name = "is_approver_required")
    private Boolean isApproverRequired = false;

    @Column(name="is_approved")
    private Boolean isApproved;
}
