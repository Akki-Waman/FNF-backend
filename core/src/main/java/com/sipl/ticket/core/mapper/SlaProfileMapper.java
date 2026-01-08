package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.SlaProfile;
import com.sipl.ticket.core.dto.request.SlaProfileRequestDto;
import com.sipl.ticket.core.dto.response.SlaProfileResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class,
                BranchMapper.class
        }
)
public interface SlaProfileMapper {

    SlaProfile toEntity(SlaProfileRequestDto dto);

    @InheritConfiguration(name = "toDto")
    SlaProfileResponseDto toDto(SlaProfile entity);

    List<SlaProfileResponseDto> mapSlaProfileListToDtoList(
            List<SlaProfile> slaProfiles
    );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "slaProfileId", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    void partialUpdate(
            SlaProfileRequestDto dto,
            @MappingTarget SlaProfile entity
    );
    List<SlaProfileResponseDto> mapSlaProfileDropListToDtoList(
            List<SlaProfile> slaProfiles
    );
}
