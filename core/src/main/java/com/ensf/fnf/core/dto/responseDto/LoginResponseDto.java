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
public class LoginResponseDto {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("jwt_token")
    private String jwtToken;

    @JsonProperty("profile_completed")
    private Boolean profileCompleted;
}