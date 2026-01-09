package com.sipl.ticket.core.dto.request;

import lombok.Data;

@Data
public class TagsSearchRequestDto extends SearchRequestDto{
    private String query;

}
