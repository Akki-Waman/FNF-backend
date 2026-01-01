package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Branches;
import com.sipl.ticket.core.dto.request.BranchRequestDto;
import com.sipl.ticket.core.dto.response.BranchDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AuditUserMasterMapper.class,
                CompanyMapper.class
        }
)
public interface BranchMapper {

    Branches toEntity(BranchRequestDto branchDto);

    @InheritConfiguration(name = "toDto")

    BranchDto toDto(Branches branches);

    List<BranchDto> mapBranchesListToDtoList(List<Branches> branchesList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    void partialUpdate(
            BranchRequestDto dto,
            @MappingTarget Branches branch
    );
}
