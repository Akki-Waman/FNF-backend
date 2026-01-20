package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "client_products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class ClientProducts extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_product_id")
    private Long clientProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Products products;

    @Column(name = "group_name")
    private String groupName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id")
    private Divisions division;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "op_unit_id")
    private OperationalUnit unit;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "platform_model")
    private String platformModel;

    @Column(name = "serial_number", unique = true)
    private String serialNumber;

    @Column(name = "imei_no", nullable = true)
    private String imeiNo;

    @Column(name = "mdm_asset_no", length = 50)
    private String mdmAssetNo;

    @Column(name = "part_no", length = 50)
    private String partNo;

    @Column(name = "working_status", length = 30)
    private String workingStatus;

    @Column(name = "device_status", length = 30)
    private String deviceStatus;

    @Column(name = "po_no", length = 50)
    private String poNo;

    @Column(name = "po_date")
    private LocalDate poDate;

    @Column(name = "remark_1", columnDefinition = "TEXT")
    private String remark1;

    @Column(name = "remark_2", columnDefinition = "TEXT")
    private String remark2;

    @Column(name = "is_warranty")
    private Boolean isWarranty = false;

    @Column(name = "warranty_start_date")
    private LocalDate warrantyPeriodStartDate;

    @Column(name = "warranty_end_date")
    private LocalDate warrantyPeriodEndDate;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
