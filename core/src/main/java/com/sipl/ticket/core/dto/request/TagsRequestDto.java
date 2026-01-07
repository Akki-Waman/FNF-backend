package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TagsRequestDto {

    private Long tagId;

    private String tagName;

    private Boolean isActive;
}
