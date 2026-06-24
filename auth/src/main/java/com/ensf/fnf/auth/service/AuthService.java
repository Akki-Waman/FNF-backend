package com.ensf.fnf.auth.service;

import com.ensf.fnf.core.dto.requestDto.CreateProfileRequestDto;
import com.ensf.fnf.core.dto.requestDto.SendOtpRequestDto;
import com.ensf.fnf.core.dto.requestDto.VerifyOtpRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.LoginResponseDto;

public interface AuthService {
    CommonApiResponse<String>
    sendOtp(
            SendOtpRequestDto dto
    );

    CommonApiResponse<LoginResponseDto>
    verifyOtp(
            VerifyOtpRequestDto dto
    );

    CommonApiResponse<String>
    createProfile(
            CreateProfileRequestDto dto
    );
}