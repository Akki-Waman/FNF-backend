package com.sipl.ticket.core.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.*;

@Getter
@Setter
@ToString
public class ContactSearchRequestDto extends SearchRequestDto {

    private Long contactId;
    private Long departmentId;
    private String query;

}
