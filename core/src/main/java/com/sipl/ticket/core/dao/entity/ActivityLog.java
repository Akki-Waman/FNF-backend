package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "activity_Logs")
public class ActivityLog extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityLogId;

    private String description;

    private String staffName;

    private Users performedBy;

    private String ipAddress;
}
