package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_response_attachments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class TicketResponseAttachment extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Long attachmentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_response_id", nullable = false)
    private TicketResponse ticketResponse;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "file_size_kb")
    private Integer fileSizeKB;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "uploaded_on", nullable = false)
    private LocalDateTime uploadedOn;

}
