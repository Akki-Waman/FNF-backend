package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Shift;
import com.sipl.ticket.core.dto.request.ShiftRequestDto;
import com.sipl.ticket.core.dto.response.ShiftResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ShiftMapper {

    @Mapping(target = "shiftId", ignore = true)
    Shift toEntity(ShiftRequestDto dto);

    ShiftResponseDTO toResponseDto(Shift shift);

    List<ShiftResponseDTO> toResponseDtoList(List<Shift> shifts);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(
            ShiftRequestDto dto,
            @MappingTarget Shift entity
    );
}

