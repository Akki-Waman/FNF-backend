package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "dms_document_metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class DmsDocumentMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_metadata_id")
    private Long documentMetadataId;

    @Column(name = "metadata_key")
    private String metadataKey;

    @Column(name = "metadata_value")
    private String metadataValue;

    @ManyToOne(optional = false)
    @JoinColumn(name = "document_id")
    private DmsDocument document;
}
