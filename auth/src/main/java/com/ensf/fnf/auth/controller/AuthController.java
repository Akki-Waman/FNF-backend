package com.ensf.fnf.auth.controller;

import com.ensf.fnf.core.dto.requestDto.CreateProfileRequestDto;
import com.ensf.fnf.core.dto.requestDto.OAuthLoginRequestDto;
import com.ensf.fnf.core.dto.requestDto.SendOtpRequestDto;
import com.ensf.fnf.core.dto.requestDto.VerifyOtpRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.LoginResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
@Api(tags = "Authentication APIs")
public interface AuthController {

    @PostMapping("/send-otp")
    ResponseEntity<
            CommonApiResponse<String>>
    sendOtp(
            @RequestBody
            SendOtpRequestDto dto
    );


    @PostMapping("/verify-otp")
    ResponseEntity<CommonApiResponse<LoginResponseDto>>
    verifyOtp(
            @RequestBody
            VerifyOtpRequestDto dto
    );

    @PostMapping("/create-profile")
    ResponseEntity<CommonApiResponse<String>>
    createProfile(
            @RequestBody
            CreateProfileRequestDto dto
    );

    @PostMapping("/oauth")
    @ApiOperation("Authenticate via Google or Apple Single Sign-On")
    ResponseEntity<CommonApiResponse<LoginResponseDto>> oauthLogin
            (@RequestBody OAuthLoginRequestDto dto);
}