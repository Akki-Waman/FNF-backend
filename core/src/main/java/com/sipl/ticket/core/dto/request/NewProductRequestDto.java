package com.sipl.ticket.core.dto.request;

import com.sipl.ticket.core.dto.response.ProductDto;
import com.sipl.ticket.core.dto.response.ProductUnitDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NewProductRequestDto {
    private ProductDto productDto;
    private List<ProductUnitDto> productUnitDtoList;
}
