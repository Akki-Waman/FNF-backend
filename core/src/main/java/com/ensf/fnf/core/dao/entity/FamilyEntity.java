package com.ensf.fnf.core.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "family")
@Getter
@Setter
public class FamilyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_id")
    private Long familyId;

    @Column(name = "family_name")
    private String familyName;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private UserEntity createdBy;

    @CreationTimestamp
    @Column(name = "created_datetime")
    private LocalDateTime createdDateTime;
}
