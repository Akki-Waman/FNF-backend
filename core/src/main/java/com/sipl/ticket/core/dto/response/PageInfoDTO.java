package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageInfoDTO {
    private Integer pageSize;
    private Integer pageNumber;
    private Long totalElements;
    private Integer numberOfElements;
    private Integer totalPages;
}
