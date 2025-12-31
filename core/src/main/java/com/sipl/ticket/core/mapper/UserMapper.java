package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Unit;
import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dto.response.UnitDto;
import com.sipl.ticket.core.dto.response.UsersResponseDTO;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AuditUserMasterMapper.class
)
public interface UserMapper {

    @InheritConfiguration(name = "toEntity")
    Users toEntity(UsersResponseDTO usersResponseDTO);

    @InheritConfiguration(name = "toDto")
    UsersResponseDTO toDto(Users users);
}
