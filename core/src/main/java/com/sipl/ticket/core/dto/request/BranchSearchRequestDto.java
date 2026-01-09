package com.sipl.ticket.core.dto.request;

import lombok.Data;

@Data
public class BranchSearchRequestDto extends SearchRequestDto{
    private String query;
}
