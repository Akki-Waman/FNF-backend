package com.sipl.ticket.core.dto.request;

import lombok.Data;

@Data
public class ShiftSearchRequestDto extends SearchRequestDto{
    private String query;
    private Integer branchId;
}
