package com.sipl.ticket.core.mapper;


import com.sipl.ticket.core.dao.entity.Products;
import com.sipl.ticket.core.dto.response.ProductDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = AuditUserMasterMapper.class)
public interface ProductMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toEntity")
        //  @Mapping(target = "isActive", ignore = true)
        //  @Mapping(target = "productSubCategory", ignore = true)
        //  @Mapping(target = "brands", ignore = true)
        //  @Mapping(target = "origins", ignore = true)
        //  @Mapping(target = "unit", ignore = true)
    Products toEntity(ProductDto productDto);

    @InheritConfiguration(name = "toDto")
    ProductDto toDto(Products products);

    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "productSubCategory", ignore = true)
    @Mapping(target = "brands", ignore = true)
    @Mapping(target = "origins", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "productCategory", ignore = true)
    @Mapping(target = "defaultTaxHead", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "branch" ,ignore=true)
    Products updateEntity(@MappingTarget Products existingEntity, ProductDto productDto);

    List<ProductDto> mapProductsListToDtoList(List<Products> productsList);

    List<ProductDto> toDtoList(List<Products> content);

}

