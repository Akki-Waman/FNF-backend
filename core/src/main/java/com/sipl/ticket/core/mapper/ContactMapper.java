package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Contact;
import com.sipl.ticket.core.dto.request.ContactRequestDto;
import com.sipl.ticket.core.dto.response.ContactResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    /* ================= CREATE ================= */

    @Mapping(target = "contactId", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "branch", ignore = true)
    //  audit fields exist in ENTITY → safe to ignore
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "modifiedTime", ignore = true)

    Contact toEntity(ContactRequestDto dto);

    /* ================= RESPONSE ================= */

    @Mapping(target = "departmentId", source = "department.departmentId")
    @Mapping(target = "departmentName", source = "department.departmentName")
    @Mapping(target = "branchId", source = "branch.branchId")
    @Mapping(target = "branchName", source = "branch.branchName")
        //  DO NOT IGNORE AUDIT FIELDS HERE (they don't exist in DTO)

    ContactResponseDto toResponseDto(Contact contact);

    /* ================= UPDATE ================= */

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "branch", ignore = true)
    //  audit fields exist in ENTITY → safe to ignore
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "modifiedTime", ignore = true)

    Contact partialUpdate(
            ContactRequestDto dto,
            @MappingTarget Contact contact
    );

    /* ================= LIST ================= */

    List<ContactResponseDto> toResponseDtoList(List<Contact> contacts);
}
