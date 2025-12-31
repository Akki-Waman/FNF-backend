package com.sipl.ticket.core.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductGetCustomDto {
	
	private Long productId;
	private String productCode;
	private String productDesc;
    private String productName;
    private Long unitId;
    private String unitName;
    private String partNumber;
    private Long slabId;
    private String description;
    private BigDecimal cgstRate;
    private BigDecimal sgstRate;
    private BigDecimal igstRate;


}
