package com.sipl.ticket.core.mapper;


import com.sipl.ticket.core.dao.entity.ProductCategories;
import com.sipl.ticket.core.dto.request.ProductCategoryRequestDto;
import com.sipl.ticket.core.dto.response.ProductCategoryDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.WARN,
        componentModel = "spring"
)
public interface ProductCategoryMapper {

    @Mapping(target = "productCategoryId", ignore = true)
    ProductCategories toEntity(ProductCategoryRequestDto productCategoryRequestDto);

    ProductCategoryDto toResponseDto(ProductCategories productCategories);

    @Mapping(target = "productCategoryId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductCategories partialUpdate(
            ProductCategoryRequestDto productCategoryRequestDto,
            @MappingTarget ProductCategories productCategories
    );

    List<ProductCategoryDto> toResponseDtoList(List<ProductCategories> productCategoriesList);
}
