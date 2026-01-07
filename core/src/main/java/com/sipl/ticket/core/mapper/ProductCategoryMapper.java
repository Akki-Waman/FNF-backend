package com.sipl.ticket.core.mapper;


import com.sipl.ticket.core.dao.entity.ProductCategories;
import com.sipl.ticket.core.dto.request.ProductCategoryRequestDto;
import com.sipl.ticket.core.dto.response.ProductCategoryDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = AuditUserMasterMapper.class)

public interface ProductCategoryMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toEntity")
    ProductCategories toEntity(ProductCategoryDto productCategoryDto);

    @InheritConfiguration(name = "toDto")
    ProductCategoryDto toDto(ProductCategories productCategories);

    List<ProductCategoryDto> mapProductCategoriesListToDtoList(
            List<ProductCategories> productCategoriesList);

    List<ProductCategoryDto> mapProductCategoriesDropListToDtoList(
            List<ProductCategories> productCategories);

    List<ProductCategoryDto> toResponseDtoList(List<ProductCategories> productCategoriesList);

    List<ProductCategoryDto> toDtoList(List<ProductCategories> content);
}
