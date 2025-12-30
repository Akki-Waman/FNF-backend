package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Branches;
import com.sipl.ticket.core.dto.response.BranchDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class,
                CompanyMapper.class
        }
)
public interface BranchMapper {

    @InheritConfiguration(name = "toEntity")
    Branches toEntity(BranchDto branchDto);

    @InheritConfiguration(name = "toDto")
    BranchDto toDto(Branches branches);

    List<BranchDto> mapBranchesListToDtoList(List<Branches> branchesList);
}
