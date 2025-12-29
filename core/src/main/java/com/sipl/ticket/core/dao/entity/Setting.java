package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "settings")
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settingId;

    private String screen;

    private String status;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "is_manual")
    private Boolean isManual;

    @Column(name = "last_number")
    private Long lastNumber;
}
