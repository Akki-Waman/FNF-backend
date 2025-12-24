package com.sipl.ticket.notificationService.controller.impl;

import com.sipl.ticket.core.dto.request.CustomerVerificationRequestDTO;
import com.sipl.ticket.core.dto.request.VerifiyOtpRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.OtpVerificationResponseDTO;
import com.sipl.ticket.core.dto.response.VerifiedOtpResponseDTO;
import com.sipl.ticket.notificationService.controller.OtpVerificationController;
import com.sipl.ticket.notificationService.service.OtpVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@RequiredArgsConstructor
@Slf4j
public class OtpVerificationControllerImpl implements OtpVerificationController {

    private final OtpVerificationService otpVerificationService;

    @Override
    public ResponseEntity<ApiResponseDTO<OtpVerificationResponseDTO>> sendOtp(String username) {
        log.info("<<Start>>sendOtp endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<OtpVerificationResponseDTO>> responseEntity =
                new ResponseEntity<>(otpVerificationService.sendOtp(username), HttpStatus.OK);
        log.info("<<End>>sendOtp endpoint called<<End>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<OtpVerificationResponseDTO>> validateUserNameAndSendOtp(CustomerVerificationRequestDTO dto) {
        log.info("<<Start>>validateUserNameAndSendOtp endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<OtpVerificationResponseDTO>> responseEntity =
                new ResponseEntity<>(otpVerificationService.validateUserNameAndSendOtp(dto), HttpStatus.OK);
        log.info("<<End>>validateUserNameAndSendOtp endpoint called<<End>>");
        return responseEntity;
    }


    @Override
    public ResponseEntity<ApiResponseDTO<VerifiedOtpResponseDTO>> validateOtp(VerifiyOtpRequestDTO verifiyOtpRequestDTO) {
        return ResponseEntity.ok(otpVerificationService.validateOtp(verifiyOtpRequestDTO));

    }

    @Override
    public ResponseEntity<ApiResponseDTO<OtpVerificationResponseDTO>> verifyMobileOtpAndRegisterUser(CustomerVerificationRequestDTO dto) {
        return ResponseEntity.ok(otpVerificationService.verifyMobileOtpAndRegisterUser(dto));

    }

    @Override
    public ResponseEntity<ApiResponseDTO<OtpVerificationResponseDTO>> sendMobileOtp(Long mobileNumber) {
        return ResponseEntity.ok(otpVerificationService.sendMobileOtp(mobileNumber));

    }
    @Override
    public ResponseEntity<ApiResponseDTO<VerifiedOtpResponseDTO>> validateMobileOtp(VerifiyOtpRequestDTO verifiyOtpRequestDTO) {
        log.info("<<Start>>validateMobileOtp endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<VerifiedOtpResponseDTO>> responseEntity =
                new ResponseEntity<>(otpVerificationService.validateMobileOtp(verifiyOtpRequestDTO), HttpStatus.OK);
        log.info("<<End>>validateMobileOtp endpoint called<<End>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<OtpVerificationResponseDTO>> sendOtpByUserId(Long userId) {
        return ResponseEntity.ok(otpVerificationService.sendOtpByUserId(userId));
    }

}
