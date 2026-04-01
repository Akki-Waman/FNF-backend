package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_sub_categories")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class ProductSubCategories extends AuditEntity {

    private static final long serialVersionUID = 5982969090309021872L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_sub_category_id")
    private Long productSubCategoryId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "product_category_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_product_sub_categories_product_category_id"))
    private ProductCategories productCategories;

    @Column(name = "product_sub_category_name")
    private String productSubCategoryName;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted = true;
}