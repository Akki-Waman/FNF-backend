package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "brands")
@Audited
public class Brands extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted = true;

}
