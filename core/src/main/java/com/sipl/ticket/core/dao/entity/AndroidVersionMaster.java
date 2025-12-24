package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "android_version")
public class AndroidVersionMaster extends AuditEntity {

    private static final long serialVersionUID = -2439012889019953775L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String appId;
    private String version;
    private String url;
    private String lastVersion;
    private boolean rStat;
}

