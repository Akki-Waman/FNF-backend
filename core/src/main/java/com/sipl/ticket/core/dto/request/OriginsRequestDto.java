package com.sipl.ticket.core.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OriginsRequestDto {

    private Long originId;
    private String originName;
    private Boolean isActive;
}

