package com.sipl.ticket.core.mapper;


import com.sipl.ticket.core.dao.entity.Companies;
import com.sipl.ticket.core.dto.response.CompanyDto;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AuditUserMasterMapper.class
)
public interface CompanyMapper extends AuditEntityMapper{
    @InheritConfiguration(name = "toEntity")
    Companies toEntity(CompanyDto companyDto);

    @InheritConfiguration(name = "toDto")
    CompanyDto toDto(Companies companies);

    List<CompanyDto> mapCompaniesListToDtoList(List<Companies> companiesList);
}
