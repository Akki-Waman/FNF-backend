package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.ServiceEntity;
import com.sipl.ticket.core.dto.request.ServiceRequestDto;
import com.sipl.ticket.core.dto.response.ServiceResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.WARN,
        componentModel = "spring"
)
public interface ServiceMapper {

    @Mapping(target = "serviceId", ignore = true)
    ServiceEntity toEntity(ServiceRequestDto serviceRequestDto);

    ServiceResponseDTO toResponseDto(ServiceEntity service);

    @Mapping(target = "serviceId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ServiceEntity partialUpdate(
            ServiceRequestDto serviceRequestDto,
            @MappingTarget ServiceEntity service
    );

    List<ServiceResponseDTO> toResponseDtoList(List<ServiceEntity> services);
}
