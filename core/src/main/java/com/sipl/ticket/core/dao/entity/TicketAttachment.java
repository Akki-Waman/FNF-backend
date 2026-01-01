package com.sipl.ticket.core.dao.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_attachments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class TicketAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Long attachmentId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_size_kb")
    private Integer fileSizeKB;

    @Column(name = "content_type")
    private String contentType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "uploaded_by")
    private Users uploadedBy;

    @Column(name = "uploaded_on", nullable = false)
    private LocalDateTime uploadedOn;

    @ManyToOne(optional = false)
    @JoinColumn(name = "document_id")
    private DmsDocument dmsDocument;
}
