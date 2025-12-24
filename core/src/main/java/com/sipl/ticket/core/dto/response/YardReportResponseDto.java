package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class YardReportResponseDto {
    private String movementName;      // Transactions table movement description
    private Long transactionId;
    private String plantCode;
    private String tag;               // Transactions → RFID.tag
    private String lepNumber;         // Transactions → VehicleAllocation.lepNumber

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime yardInTime; // TransactionStages → YARD IN
    private String yardInBy;          // TransactionStages → actionBy.userName
    private String yardInName;        // Yard table → yardName

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime loadInTime; // TransactionStages → LOADING IN
    private String loadInBy;          // TransactionStages → actionBy.userName
    private String loadInName;        // Abhi comment (not used)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime loadOutTime; // TransactionStages → LOADING OUT
    private String loadOutBy;          // TransactionStages → actionBy.userName
    private String loadOutName;        // Abhi comment (not used)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime yardOutTime; // TransactionStages → YARD OUT
    private String yardOutBy;          // TransactionStages → actionBy.userName
    private String yardOutName;

    public YardReportResponseDto(String movementName, Long transactionId, String plantCode, String tag, String lepNumber,
                                 LocalDateTime yardInTime, String yardInBy, String yardInName,
                                 LocalDateTime loadInTime, String loadInBy, String loadInName,
                                 LocalDateTime loadOutTime, String loadOutBy, String loadOutName,
                                 LocalDateTime yardOutTime, String yardOutBy, String yardOutName) {
        this.movementName = movementName;
        this.transactionId = transactionId;
        this.plantCode = plantCode;
        this.tag = tag;
        this.lepNumber = lepNumber;
        this.yardInTime = yardInTime;
        this.yardInBy = yardInBy;
        this.yardInName = yardInName;
        this.loadInTime = loadInTime;
        this.loadInBy = loadInBy;
        this.loadInName = loadInName;
        this.loadOutTime = loadOutTime;
        this.loadOutBy = loadOutBy;
        this.loadOutName = loadOutName;
        this.yardOutTime = yardOutTime;
        this.yardOutBy = yardOutBy;
        this.yardOutName = yardOutName;
    }
}
