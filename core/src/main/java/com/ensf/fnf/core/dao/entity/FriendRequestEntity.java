package com.ensf.fnf.core.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "friend_request")
@Getter
@Setter
public class FriendRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_request_id")
    private Long friendRequestId;

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id")
    private UserEntity receiver;

    @Column(name = "request_status")
    private String requestStatus;

    @CreationTimestamp
    @Column(name = "request_datetime")
    private LocalDateTime requestDateTime;
}
