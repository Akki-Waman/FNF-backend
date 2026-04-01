package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.GstSlabMaster;
import com.sipl.ticket.core.dto.response.GstSlabDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AuditUserMasterMapper.class
)
public interface GstSlabMasterMapper extends AuditEntityMapper {

    @InheritConfiguration(name = "toEntity")
    GstSlabMaster toEntity(GstSlabDto gstSlabDto);

    @InheritConfiguration(name = "toDto")
    GstSlabDto toDto(GstSlabMaster gstSlabMaster);

    List<GstSlabDto> mapGstSlabListToDtoList(List<GstSlabMaster> gstSlabMasterList);
}
