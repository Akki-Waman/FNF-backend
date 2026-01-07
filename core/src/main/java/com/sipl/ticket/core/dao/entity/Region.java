package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "region")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Region extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long regionId;

    @Column(name = "region_name", length = 100, nullable = false, unique = true)
    private String regionName;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
