package com.sipl.ticket.core.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Movements Req DTO")
public class MovementsRequestDTO {
    private Integer source;
    private Integer destination;
    private String movementDescription;
    private Boolean isSourceNet;
    private Boolean isPrimary;
}
