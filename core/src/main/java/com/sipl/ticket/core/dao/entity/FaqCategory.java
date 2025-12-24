package com.sipl.ticket.core.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "faq_category")
@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class FaqCategory extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_fategory_id")
    private Integer faqCategoryId;

    @Column(name = "category_name")
    private String categoryName;

    private Boolean isActive;

    private String categoryDescription;
}
