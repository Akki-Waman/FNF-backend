package com.ensf.fnf.auth.service;

import com.ensf.fnf.core.dto.requestDto.CreateAccountRequestDto;
import com.ensf.fnf.core.dto.requestDto.LoginRequestDto;
import com.ensf.fnf.core.dto.requestDto.SendOtpRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.LoginResponse;
import org.springframework.stereotype.Service;
@Service
public interface AuthService {

    CommonApiResponse<LoginResponse> createAccount(
            CreateAccountRequestDto dto);

    CommonApiResponse<String> sendOtp(
            SendOtpRequestDto dto
    );

    CommonApiResponse<LoginResponse> login(
            LoginRequestDto dto
    );

}