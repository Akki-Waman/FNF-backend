package com.ensf.fnf.core.dto.requestDto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendOtpRequestDto {

    private String email;

    private String mobileNumber;
}