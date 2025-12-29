package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.ProductUnit;
import com.sipl.ticket.core.dto.response.ProductUnitDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = AuditUserMasterMapper.class)
public interface ProductUnitMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toDto")
    ProductUnitDto toProductUnitDto(ProductUnit productUnit);

    List<ProductUnitDto> toProductUnitDtoList(List<ProductUnit> productUnitList);

    @InheritConfiguration(name = "toEntity")
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "unit", ignore = true)
    ProductUnit toProductUnit(ProductUnitDto productUnitDto);

    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "productUnitId", ignore = true)
    ProductUnit updateExistingProductUnit(
            @MappingTarget ProductUnit productUnit, ProductUnitDto productUnitDto);

    List<ProductUnit> toProductUnitList(List<ProductUnitDto> productUnitDtoList);
}
