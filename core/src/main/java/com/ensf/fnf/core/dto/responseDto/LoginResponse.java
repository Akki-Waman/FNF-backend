package com.ensf.fnf.core.dto.responseDto;

import com.ensf.fnf.core.dto.UserLoginView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private boolean loginSuccess;

    private String bearerToken;

    private UserLoginView user;
}