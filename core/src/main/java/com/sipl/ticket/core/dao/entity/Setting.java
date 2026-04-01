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
public class Setting extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id")
    private Long settingId;

    @Column(name = "screen")
    private String screen;

    @Column(name = "status")
    private String status;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "is_manual")
    private Boolean isManual;

    @Column(name = "last_number")
    private Long lastNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "setting_for")
    private String settingFor;

    @Column(name = "setting_key")
    private String settingKey;

    @Column(name = "setting_type")
    private String settingType;

    @Column(name = "setting_value")
    private String settingValue;

}
