package com.ensf.fnf.core.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtpResponseDto {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("jwt_token")
    private String jwtToken;

    @JsonProperty("new_user")
    private Boolean newUser;
}
