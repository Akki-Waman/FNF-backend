package com.ensf.fnf.auth.controller.impl;

import com.ensf.fnf.auth.controller.AuthController;
import com.ensf.fnf.auth.service.AuthService;
import com.ensf.fnf.core.dto.requestDto.CreateAccountRequestDto;
import com.ensf.fnf.core.dto.requestDto.LoginRequestDto;
import com.ensf.fnf.core.dto.requestDto.SendOtpRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    public ResponseEntity<CommonApiResponse<LoginResponse>>
    createAccount(
            CreateAccountRequestDto dto) {

        log.info(
                "<<START>> createAccount API called <<START>>"
        );

        CommonApiResponse<LoginResponse> response =
                authService.createAccount(dto);

        log.info(
                "<<END>> createAccount API called <<END>>"
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommonApiResponse<String>>
    sendOtp(
            SendOtpRequestDto dto) {

        log.info(
                "<<START>> sendOtp API <<START>>"
        );

        CommonApiResponse<String> response =
                authService.sendOtp(dto);

        log.info(
                "<<END>> sendOtp API <<END>>"
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<
            CommonApiResponse<LoginResponse>>
    login(
            LoginRequestDto dto) {

        log.info(
                "<<START>> login API <<START>>"
        );

        CommonApiResponse<LoginResponse> response =
                authService.login(dto);

        log.info(
                "<<END>> login API <<END>>"
        );

        return ResponseEntity.ok(response);
    }
}