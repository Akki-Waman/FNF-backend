package com.ensf.fnf.auth.controller.impl;

import com.ensf.fnf.auth.controller.AuthController;
import com.ensf.fnf.auth.service.AuthService;
import com.ensf.fnf.core.dto.requestDto.CreateProfileRequestDto;
import com.ensf.fnf.core.dto.requestDto.OAuthLoginRequestDto;
import com.ensf.fnf.core.dto.requestDto.SendOtpRequestDto;
import com.ensf.fnf.core.dto.requestDto.VerifyOtpRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthControllerImpl
        implements AuthController {

    private final AuthService authService;

    @Override
    public ResponseEntity<
            CommonApiResponse<String>>
    sendOtp(
            SendOtpRequestDto dto) {

        log.info(
                "AuthControllerImpl :: sendOtp :: START"
        );

        CommonApiResponse<String> response =
                authService.sendOtp(
                        dto
                );

        log.info(
                "AuthControllerImpl :: sendOtp :: END"
        );

        return ResponseEntity.ok(
                response
        );
    }

    @Override
    public ResponseEntity<
            CommonApiResponse<LoginResponseDto>>
    verifyOtp(
            VerifyOtpRequestDto dto) {

        log.info(
                "AuthControllerImpl :: verifyOtp :: START"
        );

        CommonApiResponse<LoginResponseDto>
                response =
                authService.verifyOtp(
                        dto
                );

        log.info(
                "AuthControllerImpl :: verifyOtp :: END"
        );

        return ResponseEntity.ok(
                response
        );
    }

    @Override
    public ResponseEntity<
            CommonApiResponse<String>>
    createProfile(
            CreateProfileRequestDto dto) {

        log.info(
                "AuthControllerImpl :: createProfile :: START"
        );

        CommonApiResponse<String> response =
                authService.createProfile(
                        dto
                );

        log.info(
                "AuthControllerImpl :: createProfile :: END"
        );

        return ResponseEntity.ok(
                response
        );
    }

    @Override
    public ResponseEntity<CommonApiResponse<LoginResponseDto>> oauthLogin(OAuthLoginRequestDto dto) {
        log.info("<<START>> AuthControllerImpl :: oauthLogin <<START>>");
        CommonApiResponse<LoginResponseDto> response = authService.processOAuthLogin(dto);
        log.info("<<END>> AuthControllerImpl :: oauthLogin <<END>>");
        return ResponseEntity.ok(response);
    }
}