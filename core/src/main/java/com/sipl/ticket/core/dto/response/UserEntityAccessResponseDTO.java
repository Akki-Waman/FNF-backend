package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntityAccessResponseDTO {

    private Long userId;
    private String userName;
    private Long roleId;
    private String RoleName;
    private List<String> plantCode;
    List<MasterScreenAccessResponseDTO> accessEntityList;
}



