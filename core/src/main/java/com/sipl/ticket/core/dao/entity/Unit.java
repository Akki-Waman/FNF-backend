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
@Table(name = "units")
@Audited
public class Unit extends AuditEntity {

    private static final long serialVersionUID = -6172230680770128656L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long unitId;

    private String unitName;

    private Boolean isActive;

    public Unit(Long unitId) {
        this.unitId = unitId;
    }

    @Column(name = "is_delete")
    private Boolean isDelete = true;
}