package com.ensf.fnf.core.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "friendship")
@Getter
@Setter
public class FriendshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendship_id")
    private Long friendshipId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "friend_user_id")
    private UserEntity friendUser;

    @CreationTimestamp
    @Column(name = "connected_datetime")
    private LocalDateTime connectedDateTime;
}
