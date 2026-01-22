package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Contact;
import com.sipl.ticket.core.dto.request.ContactRequestDto;
import com.sipl.ticket.core.dto.response.ContactResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = AuditUserMasterMapper.class)
public interface ContactMapper extends AuditEntityMapper {

    @Mapping(target = "contactId", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "modifiedTime", ignore = true)
    Contact toEntity(ContactRequestDto dto);

    @Mapping(target = "branchId", source = "branch.branchId")
    @Mapping(target = "branchName", source = "branch.branchName")
    ContactResponseDto toResponseDto(Contact contact);

    List<ContactResponseDto> toResponseDtoList(List<Contact> contacts);


}
