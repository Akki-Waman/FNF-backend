package com.ensf.fnf.core.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendOtpRequestDto {

    @JsonProperty("username")
    private String username;
}