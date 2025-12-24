package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TransactionReportResponseDto {

    private String status;
    private String movement;        // Transactions → movement_description
    private Long transactionId;
    private String plantCode;                    // plant code
    private String rfidNumber;                 // RFID.tag
    private String lepNumber;                  // VehicleAllocation.lepNumber
    private String salesOrders;                // aggregated sales orders
    private String childOrders;                // aggregated child orders
    private String items;                      // items involved
    private Double orderQuantity;              // quantity
    private String vehicleRegistrationNumber;  // vehicle details
    private String driverName;
    private String customerName;

    private String yardInName;                 // yard name
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime yardInTime;

    private String gateInName;                 // gate name
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime gateInTime;

    private Double tareWeight;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime tareWeightTime;

    private Double grossWeight;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime grossWeightTime;

    private Double netWeight;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime netWeightTime;

    private String loadInLocation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime loadInTime;

    private String loadOutLocation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime loadOutTime;

    private String gateOutName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime gateOutTime;

    private String yardOutName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime yardOutTime;

    public TransactionReportResponseDto(String status, String movement, Long transactionId,
                                        String plantCode, String rfidNumber, String lepNumber,
                                        String salesOrders, String childOrders, String items, Double orderQuantity,
                                        String vehicleRegistrationNumber, String driverName, String customerName,
                                        String yardInName, LocalDateTime yardInTime,
                                        String gateInName, LocalDateTime gateInTime,
                                        Double tareWeight, LocalDateTime tareWeightTime,
                                        Double grossWeight, LocalDateTime grossWeightTime,
                                        Double netWeight, LocalDateTime netWeightTime,
                                        LocalDateTime loadInTime, String loadInLocation,
                                        LocalDateTime loadOutTime, String loadOutLocation,
                                        LocalDateTime gateOutTime, String gateOutName,
                                        LocalDateTime yardOutTime, String yardOutName) {
        this.status = status;
        this.movement = movement;
        this.transactionId = transactionId;
        this.plantCode = plantCode;
        this.rfidNumber = rfidNumber;
        this.lepNumber = lepNumber;
        this.salesOrders = salesOrders;
        this.childOrders = childOrders;
        this.items = items;
        this.orderQuantity = orderQuantity;
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
        this.driverName = driverName;
        this.customerName = customerName;
        this.yardInName = yardInName;
        this.yardInTime = yardInTime;
        this.gateInName = gateInName;
        this.gateInTime = gateInTime;
        this.tareWeight = tareWeight;
        this.tareWeightTime = tareWeightTime;
        this.grossWeight = grossWeight;
        this.grossWeightTime = grossWeightTime;
        this.netWeight = netWeight;
        this.netWeightTime = netWeightTime;
        this.loadInTime = loadInTime;
        this.loadInLocation = loadInLocation;
        this.loadOutTime = loadOutTime;
        this.loadOutLocation = loadOutLocation;
        this.gateOutTime = gateOutTime;
        this.gateOutName = gateOutName;
        this.yardOutTime = yardOutTime;
        this.yardOutName = yardOutName;
    }
}

