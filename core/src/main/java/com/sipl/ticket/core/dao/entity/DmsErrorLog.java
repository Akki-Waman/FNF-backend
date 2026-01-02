package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dms_error_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DmsErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "error_log_id")
    private Long errorLogId;

    @Column(name = "application_id")
    private Long applicationId;

    @Column(name = "class_name")
    private String className;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}

