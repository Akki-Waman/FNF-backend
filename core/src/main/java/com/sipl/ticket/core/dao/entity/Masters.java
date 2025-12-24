package com.sipl.ticket.core.dao.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "masters")
public class Masters extends Auditable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="master_id")
    private Long id;

    @Column(name = "tbl_name")
    private String tblName;

    @Column(name = "column_name")
    private String columnName;

    @Column(name = "column_code")
    private Integer columnCode;

    @Column(name = "column_value")
    private Integer columnValue;

    @Column(name = "value_desc")
    private String valueDesc;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "is_active")
    private Boolean isActive;
}
