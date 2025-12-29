package com.sipl.ticket.core.dao.entity;

import io.lettuce.core.GeoArgs;
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
@Table(name = "product_units")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class ProductUnit extends AuditEntity {

    private static final long serialVersionUID = -3963767262536366017L;

    @Id
    @Column(name = "product_unit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productUnitId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "FK_product_units_product_id"))
    private Products product;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "unit_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_product_units_unit_id"))
    private GeoArgs.Unit unit;

    @Column(name = "c_factor", nullable = false)
    private Double cFactor;

    private Boolean isSellingUnit;

    private Boolean isPurchaseUnit;

    private Double purchasePrice;

    private Double salesPrice;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(name = "length", nullable = true)
    private Double length;

    @Column(name = "height", nullable = true)
    private Double height;

    @Column(name = "width", nullable = true)
    private Double width;

    @Column(name = "cbm_value", nullable = true)
    private Double cbmValue;
}