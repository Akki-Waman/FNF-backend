package com.sipl.ticket.core.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Table(name = "countries")
@Audited
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Country extends AuditEntity {

    private static final long serialVersionUID = 1970666713575846330L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Long countryId;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "tax_type")
    private String taxType;

    @Column(name = "is_foreign")
    private Boolean isForeign;

    @Column(name = "is_active")
    private Boolean isActive;
}