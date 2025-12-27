package com.sipl.ticket.core.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientRequestDto  {

    private Long clientId;
    private String clientCode;
    private String clientName;
    private Boolean isActive;
}