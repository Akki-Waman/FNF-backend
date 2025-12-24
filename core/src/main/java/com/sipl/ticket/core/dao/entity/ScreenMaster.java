package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "screen_master")
public class ScreenMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long screenMasterId;

    private String screenName;
}
