package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class AuditEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_by")
    private Users createdBy;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "modified_by")
    private Users modifiedBy;

    @CreatedDate
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdTime;

    @LastModifiedDate
    @Column(name = "modified_on", updatable = false)
    private LocalDateTime modifiedTime;
}