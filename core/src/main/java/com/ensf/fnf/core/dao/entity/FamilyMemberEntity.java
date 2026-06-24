package com.ensf.fnf.core.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "family_member")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_member_id")
    private Long familyMemberId;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private FamilyEntity family;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "relationship_type")
    private String relationshipType;

    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "profile_photo_url")
    private String profilePhotoUrl;

    @ManyToOne
    @JoinColumn(name = "parent_member_id")
    private FamilyMemberEntity parentMember;
}