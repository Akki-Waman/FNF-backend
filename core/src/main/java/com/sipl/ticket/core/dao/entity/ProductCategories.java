package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "product_categories")
@Audited
public class ProductCategories extends AuditEntity {

    private static final long serialVersionUID = 5882131931582226105L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_category_id")
    private Long productCategoryId;

    @Column(name = "product_category_name")
    private String productCategoryName;

    @Column(name = "is_active")
    private Boolean isActive;
}