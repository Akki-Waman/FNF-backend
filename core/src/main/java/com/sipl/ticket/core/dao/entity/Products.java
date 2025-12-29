package com.sipl.ticket.core.dao.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "products")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Products extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_desc")
    private String productDesc;

    @Column(name = "product_name", length = 1000, nullable = false)
    private String productName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "product_sub_category_id",
            foreignKey = @ForeignKey(name = "fk_products_product_sub_category_id"))
    private ProductSubCategories productSubCategory;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "product_category_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_products_product_category_id"))
    private ProductCategories productCategory;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "brand_id", foreignKey = @ForeignKey(name = "fk_products_brand_id"))
    private Brands brands;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "origin_id", foreignKey = @ForeignKey(name = "fk_products_origin_id"))
    private Origins origins;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "default_unit_id",
            foreignKey = @ForeignKey(name = "fk_products_default_unit_id"))
    private Units unit;

    @ManyToOne
    @JoinColumn(
            name = "default_account_id",
            nullable = true,
            foreignKey = @ForeignKey(name = "fk_products_default_account_id"))
    private Account account;

    @Column(name = "reorder_level")
    private Long reorderLevel;

    @Column(name = "reorder_qty")
    private Long reorderQty;

    @Column(name = "min_level")
    private Double minLevel;

    @Column(name = "max_level")
    private Double maxLevel;

    @Column(name = "net_weight")
    private Double netWeight;

    @Column(name = "gross_weight")
    private Double grossWeight;

    @Column(name = "with_batch_tracking")
    private Boolean withBatchTracking;

    @Column(name = "with_expiry_date")
    private Boolean withExpiryDate;

    @Column(name = "is_service")
    private Boolean isService = false;

    @Column(name = "document_path")
    private String fileName;

    @Column(name = "is_active")
    private Boolean isActive = true;

    private Integer qcCategory;

    private String hsn;

    public Products(Long productId) {
        this.productId = productId;
    }

    @Column(name = "product_short_name")
    private String productShortName;

    @Column(name = "part_no")
    private String partNumber;

    @ManyToOne
    @JoinColumn(
            name = "default_tax_head_id",
            foreignKey = @ForeignKey(name = "fk_products_default_tax_head_id"))
    private GstSlabMaster defaultTaxHead;

    private Long dmsDocId;

    private Boolean isSync;
}

