package com.sipl.ticket.core.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportFilterDTO {

    private String search;

    private List<String> status;

    private List<Integer> priority;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdTo;
}