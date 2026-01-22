package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.ServiceEntity;
import com.sipl.ticket.core.dto.request.ServiceRequestDto;
import com.sipl.ticket.core.dto.response.ServiceResponseDTO;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AuditUserMasterMapper.class
)
public interface ServiceMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toEntity")
    ServiceEntity toEntity(ServiceRequestDto serviceRequestDto);

    @InheritConfiguration(name = "toDto")
    @Mapping(source = "company.companyId", target = "companyId")
    @Mapping(source = "company.companyName", target = "companyName")
    ServiceResponseDTO toDto(ServiceEntity service);

    List<ServiceResponseDTO> mapServicesListToDtoList(List<ServiceEntity> services);

    List<ServiceResponseDTO> mapServicesDropListToDtoList(List<ServiceEntity> services);
}
