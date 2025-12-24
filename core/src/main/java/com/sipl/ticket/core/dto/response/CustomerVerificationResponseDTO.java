package com.sipl.ticket.core.dto.response;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerVerificationResponseDTO {
    private String mobileNumber;
    private String email;
    private String username;
}
