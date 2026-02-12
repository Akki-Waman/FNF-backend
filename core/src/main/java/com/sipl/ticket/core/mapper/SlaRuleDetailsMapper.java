package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.SlaRuleDetails;
import com.sipl.ticket.core.dto.response.SlaRuleDetailsDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                SlaProfileMapper.class,
                ServiceMapper.class,
                AuditUserMasterMapper.class,
        }
)
public interface SlaRuleDetailsMapper {

    SlaRuleDetails toEntity(SlaRuleDetailsDto dto);

    @InheritConfiguration(name = "toEntity")
     SlaRuleDetailsDto toDto(SlaRuleDetails entity);

    List<SlaRuleDetailsDto> mapSlaRuleDetailsListToDtoList(
            List<SlaRuleDetails> slaRuleDetails
    );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "slaRuleDetailId", ignore = true)
    @Mapping(target = "slaProfile", ignore = true)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "modifiedTime", ignore = true)
    void partialUpdate(
            SlaRuleDetailsDto dto,
            @MappingTarget SlaRuleDetails entity
    );
}
