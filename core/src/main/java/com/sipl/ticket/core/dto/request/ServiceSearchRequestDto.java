package com.sipl.ticket.core.dto.request;

import lombok.Data;

@Data
public class ServiceSearchRequestDto extends SearchRequestDto {

    private String query;
}