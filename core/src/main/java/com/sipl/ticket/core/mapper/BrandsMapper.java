package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Brands;
import com.sipl.ticket.core.dto.response.BrandDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AuditUserMasterMapper.class
)
public interface BrandsMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toEntity")
    Brands toEntity(BrandDto brandDto);

    @InheritConfiguration(name = "toDto")
    BrandDto toDto(Brands brands);

    List<BrandDto> mapBrandsListToDtoList(List<Brands> brandsList);

    List<BrandDto> mapBrandsDropListToDtoList(List<Brands> brands);
}
