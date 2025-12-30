package com.sipl.ticket.core.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Location Request DTO")
public class LocationRequestDTO {

    @NotBlank(message = "Location Name is required")
    @Size(max = 100, message = "Location Name must not exceed 100 characters")
    @Schema(description = "Location Name", example = "Warehouse A")
    private String locationName;

    @NotNull(message = "Location Type is required")
    @Schema(description = "Location Type (e.g., Warehouse, Yard, Gate, Dock)", example = "Yard")
    private String locationType;


//    @DecimalMin(value = "0.0", inclusive = false, message = "Location Capacity must be greater than 0")
//    @Digits(integer = 15, fraction = 2, message = "Location Capacity must be a valid decimal number")
//    @Schema(description = "Location Capacity", example = "5000.00")
//    private BigDecimal locationCapacity;

    private Boolean isActive;

    private Long plantId;

}