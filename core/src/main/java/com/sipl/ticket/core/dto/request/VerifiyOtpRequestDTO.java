package com.sipl.ticket.core.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VerifiyOtpRequestDTO {
    private Long otpVerificationId;
    private String emailId;
    private String emailOtp;
    private Long mobileNumber;
    private String mobileOtp;
}