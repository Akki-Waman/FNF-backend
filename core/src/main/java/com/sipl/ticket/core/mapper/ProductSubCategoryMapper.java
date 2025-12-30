package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.ProductCategories;
import com.sipl.ticket.core.dao.entity.ProductSubCategories;
import com.sipl.ticket.core.dto.request.ProductSubCategoryRequestDto;
import com.sipl.ticket.core.dto.response.ProductCategoryDto;
import com.sipl.ticket.core.dto.response.ProductSubCategoryDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = AuditUserMasterMapper.class)

public interface ProductSubCategoryMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toEntity")
    ProductSubCategories toEntity(ProductSubCategoryDto productSubCategoryDto);

    ProductCategories toEntity(ProductCategoryDto productCategoriesDto);

    @InheritConfiguration(name = "toDto")
    ProductSubCategoryDto toDto(ProductSubCategories productSubCategories);

    List<ProductSubCategoryDto> mapProductSubCategoriesListToDtoList(
            List<ProductSubCategories> productSubCategories);

    List<ProductSubCategoryDto> toResponseDtoList(List<ProductSubCategories> entities);

}
