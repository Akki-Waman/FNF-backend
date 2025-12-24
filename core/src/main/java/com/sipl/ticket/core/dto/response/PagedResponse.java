package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T> {
    private List<T> content;
    private int currentPage;
    private long totalItems;
    private int totalPages;
    private int pageSize;
    private boolean isLastPage;
}
