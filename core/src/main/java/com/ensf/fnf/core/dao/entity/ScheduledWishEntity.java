package com.ensf.fnf.core.dao.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_wishes", indexes = {
        @Index(name = "idx_wish_delivery_status", columnList = "wish_status, scheduled_datetime"),
        @Index(name = "idx_wish_sender", columnList = "sender_user_id, scheduled_wish_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledWishEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduled_wish_id")
    private Long scheduledWishId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_user_id", nullable = false)
    private UserEntity sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_member_id", nullable = false)
    private FamilyMemberEntity receiver;

    @Column(name = "wish_message", nullable = false, length = 1000)
    private String wishMessage;

    @Column(name = "scheduled_datetime", nullable = false)
    private LocalDateTime scheduledDateTime;

    @Column(name = "wish_status", nullable = false, length = 20)
    private String wishStatus; // PENDING, SENT, FAILED

    @Column(name = "media_template_url", length = 255)
    private String mediaTemplateUrl;
}