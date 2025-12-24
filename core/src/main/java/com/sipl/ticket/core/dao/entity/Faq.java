package com.sipl.ticket.core.dao.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "faq")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Faq extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer faqId;

    @ManyToOne
    @JoinColumn(name = "faq_category_id")
    private FaqCategory faqCategory;

    private String question;
    private String answer;
    private String attachmentPath;
    private String imagePath;
    private Boolean isActive;
}
