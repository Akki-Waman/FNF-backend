package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicReportResponseDTO<T>{
    private List<String> headers;
    private List<T> dataList;
    private int pageNumber;
    private long totalElements;
    private int totalPages;
    private int pageSize;
    private boolean lastPage;
}
