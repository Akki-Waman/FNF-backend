package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Contact;
import com.sipl.ticket.core.dto.request.ContactRequestDto;
import com.sipl.ticket.core.dto.response.ContactResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ContactMapper {

    /* ================= CREATE ================= */

    @Mapping(target = "contactId", ignore = true)
    @Mapping(target = "department", ignore = true) // set in ServiceImpl
    @Mapping(target = "isActive", ignore = true)   // set in ServiceImpl
    Contact toEntity(ContactRequestDto dto);

    /* ================= RESPONSE ================= */

    @Mapping(target = "departmentName", source = "department.departmentName")
    ContactResponseDto toResponseDto(Contact contact);

    /* ================= UPDATE (PARTIAL) ================= */

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "contactId", ignore = true)
    @Mapping(target = "department", ignore = true) // set in ServiceImpl
    @Mapping(target = "isActive", ignore = true)
    Contact partialUpdate(
            ContactRequestDto dto,
            @MappingTarget Contact contact
    );

    List<ContactResponseDto> toResponseDtoList(List<Contact> contacts);
}
