package com.sipl.ticket.core.mapper;

import com.sipl.ticket.core.dao.entity.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditUserMasterMapper {

    default String toUserName(Users user) {
        return user != null ? user.getUserName() : null;
    }

    default Users toUserMaster(String username) {
        if (username == null) return null;
        Users user = new Users();
        user.setUserName(username);
        return user;
    }
}
