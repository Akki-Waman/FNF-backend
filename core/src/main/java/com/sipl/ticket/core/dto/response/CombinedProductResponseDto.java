package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CombinedProductResponseDto {
    private ProductDto productDto;
    private List<ProductUnitDto> productUnitDtoList;

}
