package com.ensf.fnf.core.dao.entity;

import com.ensf.fnf.core.enums.Gender;
import com.ensf.fnf.core.enums.RelationshipType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "family_members")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyMemberEntity
        extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private RelationshipType relationshipType;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    private LocalDate birthDate;

    private LocalDate anniversaryDate;

    private Boolean married;

    private String spouseName;

    private LocalDate spouseBirthDate;

    private LocalDate spouseAnniversaryDate;

    private String mobileNumber;

    private String email;

    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private UserEntity user;
}