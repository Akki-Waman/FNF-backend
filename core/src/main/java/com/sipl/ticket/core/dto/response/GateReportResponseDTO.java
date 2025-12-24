package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GateReportResponseDTO {
    private String status;
    private String movement;
    private Long transactionId;
    private String plantCode;
    private String tagNumber;
    private String lepNumber;
    private String truckNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime gateInTime;
    private String gateInBy;
    private String gateInName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime gateOutTime;
    private String gateOutBy;
    private String gateOutName;
}
