package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Brands;
import com.sipl.ticket.core.dto.request.BrandRequestDto;
import com.sipl.ticket.core.dto.response.BrandDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.WARN,
        componentModel = "spring"
)
public interface BrandMapper {

    @Mapping(target = "brandId", ignore = true)
    Brands toEntity(BrandRequestDto brandRequestDto);

    BrandDto toResponseDto(Brands brands);

    @Mapping(target = "brandId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Brands partialUpdate(
            BrandRequestDto brandRequestDto,
            @MappingTarget Brands brands
    );

    List<BrandDto> toResponseDtoList(List<Brands> brandsList);
}
