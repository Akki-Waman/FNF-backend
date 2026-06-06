package com.ensf.fnf.auth.controller;

import com.ensf.fnf.core.dto.requestDto.CreateAccountRequestDto;
import com.ensf.fnf.core.dto.requestDto.LoginRequestDto;
import com.ensf.fnf.core.dto.requestDto.SendOtpRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.LoginResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
@Api(tags = "Auth APIs")
public interface AuthController {

    @ApiOperation(
            value = "Create User Account",
            response = LoginResponse.class
    )
    @PostMapping("/create-account")
    ResponseEntity<CommonApiResponse<LoginResponse>>
    createAccount(
            @RequestBody CreateAccountRequestDto dto
    );

    @ApiOperation(
            value = "Send OTP"
    )
    @PostMapping("/send-otp")
    ResponseEntity<CommonApiResponse<String>>
    sendOtp(
            @RequestBody SendOtpRequestDto dto
    );

    @ApiOperation(
            value = "Login With OTP",
            response = LoginResponse.class
    )
    @PostMapping("/login")
    ResponseEntity<CommonApiResponse<LoginResponse>>
    login(
            @RequestBody LoginRequestDto dto
    );

}