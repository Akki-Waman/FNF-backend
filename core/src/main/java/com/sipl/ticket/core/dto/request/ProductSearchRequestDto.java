package com.sipl.ticket.core.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class ProductSearchRequestDto extends SearchRequestDto {

    private List<Long> productId;
    private List<Long> brandId;
    private List<Long> originId;
    private List<Long> productCategoryId;
    private List<Long> productSubCategoryId;


}