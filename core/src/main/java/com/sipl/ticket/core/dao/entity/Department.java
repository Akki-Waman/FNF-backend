package com.sipl.ticket.core.dao.entity;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
public class Department extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "department_name", length = 150, nullable = true)
    private String departmentName;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_delete",nullable = false)
    private Boolean isDelete = true;
}
