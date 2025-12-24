package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifiedOtpResponseDTO {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String userName;
    private String password;
}
