package com.ensf.fnf.core.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "event")
@Getter
@Setter
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @ManyToOne
    @JoinColumn(name = "family_member_id")
    private FamilyMemberEntity familyMember;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "event_title")
    private String eventTitle;

    @Column(name = "event_description")
    private String eventDescription;

    @Column(name = "event_date")
    private LocalDate eventDate;
}
