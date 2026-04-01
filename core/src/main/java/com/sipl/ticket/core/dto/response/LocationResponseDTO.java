package com.sipl.ticket.core.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class LocationResponseDTO extends AuditDto {

    private Long locationId;

    @NotBlank(message = "Location name is required")
    @Size(min = 2, max = 100, message = "Location name must be between 2 and 100 characters")
    private String locationName;
  //  private String  locationType;
 //   private BigDecimal locationCapacity;
    private Boolean isActive;
//    private Long plantId;
//    private String plantCode;

    private Boolean isDeleted;

    private Integer branchId;
    private String branchName;
}
