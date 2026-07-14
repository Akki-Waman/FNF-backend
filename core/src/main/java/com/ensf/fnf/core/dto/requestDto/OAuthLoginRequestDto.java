package com.ensf.fnf.core.dto.requestDto;
import lombok.Data;
@Data
public class OAuthLoginRequestDto {
    private String provider; // "GOOGLE" or "APPLE"
    private String providerToken;
    private String email;
    private String fullName;
}