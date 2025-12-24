package com.sipl.ticket.core.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Master Request DTO")
public class MastersRequestDTO {
    private Long id;

    private String tblName;

    private String columnName;

    private Integer columnCode;

    private Integer columnValue;

    private String valueDesc;

    private Integer sequence;

    private Boolean isActive;
}
