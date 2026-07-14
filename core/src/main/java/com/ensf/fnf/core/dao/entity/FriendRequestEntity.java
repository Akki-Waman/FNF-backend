package com.ensf.fnf.core.dao.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "friend_requests", indexes = {
        @Index(name = "idx_receiver_status", columnList = "receiver_user_id, request_status"),
        @Index(name = "idx_sender_receiver", columnList = "sender_user_id, receiver_user_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_request_id")
    private Long friendRequestId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_user_id", nullable = false)
    private UserEntity sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_user_id", nullable = false)
    private UserEntity receiver;

    @Column(name = "request_status", nullable = false, length = 20)
    private String requestStatus; // PENDING, ACCEPTED, DECLINED

    @CreationTimestamp
    @Column(name = "request_datetime", updatable = false)
    private LocalDateTime requestDateTime;
}