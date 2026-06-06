package com.ensf.fnf.core.dto.responseDto;

import com.ensf.fnf.core.dto.AuditDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto extends AuditDto {

    private Long id;

    private String fullName;

    private String email;

    private String mobileNumber;

    private boolean verified;
}