package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductUnitDto extends AuditDto {

    private Long productUnitId;

    private ProductDto product;

    private UnitDto unit;

    @JsonProperty("cFactor")
    private BigDecimal cFactor;

    private Boolean isSellingUnit;

    private Boolean isPurchaseUnit;

    private Double purchasePrice;

    private Double salesPrice;

    private Boolean isActive;

    private Double length;

    private Double height;

    private Double width;

    private Double cbmValue;
}