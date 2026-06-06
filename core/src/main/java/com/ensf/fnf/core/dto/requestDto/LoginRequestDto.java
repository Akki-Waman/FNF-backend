package com.ensf.fnf.core.dto.requestDto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    private String email;

    private String mobileNumber;

    private String otp;
}