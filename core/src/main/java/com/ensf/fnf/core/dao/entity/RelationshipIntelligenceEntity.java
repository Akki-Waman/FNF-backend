package com.ensf.fnf.core.dao.entity;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "relationship_intelligence")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelationshipIntelligenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intelligence_id")
    private Long intelligenceId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(name = "relationship_score", nullable = false)
    private Integer relationshipScore; // Metric scale tracking from 0 to 100

    @Column(name = "people_not_wished", nullable = false)
    private Integer peopleNotWished;

    @Column(name = "upcoming_celebrations_count", nullable = false)
    private Integer upcomingCelebrationsCount;

    @Column(name = "friend_activity_tier", nullable = false, length = 20) // HIGH, MEDIUM, LOW
    private String friendActivityTier;

    @UpdateTimestamp
    @Column(name = "last_calculated_at", nullable = false)
    private LocalDateTime lastCalculatedAt;
}