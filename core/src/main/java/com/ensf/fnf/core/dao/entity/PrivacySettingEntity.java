package com.ensf.fnf.core.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "privacy_setting")
@Getter
@Setter
public class PrivacySettingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "privacy_setting_id")
    private Long privacySettingId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "allow_family_visibility")
    private Boolean allowFamilyVisibility;

    @Column(name = "allow_friend_visibility")
    private Boolean allowFriendVisibility;

    @Column(name = "allow_relative_visibility")
    private Boolean allowRelativeVisibility;
}
