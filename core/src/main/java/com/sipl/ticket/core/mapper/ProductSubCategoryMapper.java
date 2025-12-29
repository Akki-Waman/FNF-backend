package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.ProductCategories;
import com.sipl.ticket.core.dao.entity.ProductSubCategories;
import com.sipl.ticket.core.dto.request.ProductSubCategoryRequestDto;
import com.sipl.ticket.core.dto.response.ProductSubCategoryDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ProductSubCategoryMapper {

    @Mapping(target = "productSubCategoryId", ignore = true)
    @Mapping(source = "productCategoryId", target = "productCategories")
    ProductSubCategories toEntity(ProductSubCategoryRequestDto requestDto);

    ProductSubCategoryDto toResponseDto(ProductSubCategories entity);

    @Mapping(target = "productSubCategoryId", ignore = true)
    @Mapping(source = "productCategoryId", target = "productCategories")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductSubCategories partialUpdate(
            ProductSubCategoryRequestDto requestDto,
            @MappingTarget ProductSubCategories entity
    );

    List<ProductSubCategoryDto> toResponseDtoList(List<ProductSubCategories> entities);

    /* 🔹 Helper mapping: categoryId → ProductCategories */
    default ProductCategories map(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        ProductCategories category = new ProductCategories();
        category.setProductCategoryId(categoryId);
        return category;
    }
}
