package com.ensf.fnf.core.dao.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "memories", indexes = {
        @Index(name = "idx_user_memory_timeline", columnList = "user_id, memory_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memory_id")
    private Long memoryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "media_url", nullable = false, length = 512)
    private String mediaUrl;

    @Column(name = "media_type", nullable = false, length = 20) // PHOTO, VIDEO
    private String mediaType;

    @Column(name = "caption", length = 500)
    private String caption;

    @Column(name = "album_name", length = 100)
    private String albumName; // Nullable parameter if it's an unassigned loose grid item

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}