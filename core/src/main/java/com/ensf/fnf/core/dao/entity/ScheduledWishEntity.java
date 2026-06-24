package com.ensf.fnf.core.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_wish")
@Getter
@Setter
public class ScheduledWishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduled_wish_id")
    private Long scheduledWishId;

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "receiver_member_id")
    private FamilyMemberEntity receiver;

    @Column(name = "wish_message", length = 1000)
    private String wishMessage;

    @Column(name = "scheduled_datetime")
    private LocalDateTime scheduledDateTime;

    @Column(name = "wish_status")
    private String wishStatus;
}
