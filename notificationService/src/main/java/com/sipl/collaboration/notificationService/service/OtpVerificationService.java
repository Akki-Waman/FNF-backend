package com.sipl.ticket.notificationService.service;

import com.sipl.ticket.core.dto.request.CustomerVerificationRequestDTO;
import com.sipl.ticket.core.dto.request.VerifiyOtpRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.OtpVerificationResponseDTO;
import com.sipl.ticket.core.dto.response.VerifiedOtpResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface OtpVerificationService {
    ApiResponseDTO<OtpVerificationResponseDTO> sendOtp(String username);

    ApiResponseDTO<VerifiedOtpResponseDTO> validateOtp(VerifiyOtpRequestDTO verifiyOtpRequestDTO);

    ApiResponseDTO<OtpVerificationResponseDTO> validateUserNameAndSendOtp(CustomerVerificationRequestDTO dto);

    ApiResponseDTO<OtpVerificationResponseDTO> verifyMobileOtpAndRegisterUser(CustomerVerificationRequestDTO dto);

    ApiResponseDTO<OtpVerificationResponseDTO> sendMobileOtp(Long mobileNumber);

    ApiResponseDTO<VerifiedOtpResponseDTO> validateMobileOtp(VerifiyOtpRequestDTO verifiyOtpRequestDTO);

    ApiResponseDTO<OtpVerificationResponseDTO> sendOtpByUserId(Long userId);
}
