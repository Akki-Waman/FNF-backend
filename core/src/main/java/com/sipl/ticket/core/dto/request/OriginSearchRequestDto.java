package com.sipl.ticket.core.dto.request;

import lombok.Data;

@Data
public class OriginSearchRequestDto extends SearchRequestDto {
    private Long originId;
}
