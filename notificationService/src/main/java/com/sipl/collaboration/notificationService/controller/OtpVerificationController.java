package com.sipl.ticket.notificationService.controller;

import com.sipl.ticket.core.dto.request.CustomerVerificationRequestDTO;
import com.sipl.ticket.core.dto.request.VerifiyOtpRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.OtpVerificationResponseDTO;
import com.sipl.ticket.core.dto.response.VerifiedOtpResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/notification")
@CrossOrigin("*")
@Api(tags = "Otp Verification")
public interface OtpVerificationController {

    @GetMapping("/send-otp/{username}")
    @ApiOperation(
            value = "Send OTP to user's registered email",
            notes = "This API fetches the user by username, generates an OTP, and sends it to the user's registered email address for verification."
    )
    public ResponseEntity<ApiResponseDTO<OtpVerificationResponseDTO>> sendOtp(@PathVariable String username);

    @PostMapping("/validate-otp")
    @ApiOperation(
            value = "Validate email OTP and verify user",
            notes = "This API verifies the OTP sent to the user's email. If the OTP is valid and not expired, it marks the OTP as verified and returns basic user details."
    )
    ResponseEntity<ApiResponseDTO<VerifiedOtpResponseDTO>> validateOtp(@RequestBody VerifiyOtpRequestDTO verifiyOtpRequestDTO);


    @PostMapping("/send/mobile-otp")
    @ApiOperation(
            value = "Validate username and send mobile OTP",
            notes = "This API validates if the provided username is available. If available, it generates and sends an OTP to the provided mobile number for verification."
    )
    public ResponseEntity<ApiResponseDTO<OtpVerificationResponseDTO>> validateUserNameAndSendOtp(
            @RequestBody CustomerVerificationRequestDTO dto
    );

    @PostMapping("/verify/mobile-otp")
    @ApiOperation(
            value = "Verify mobile OTP and register user",
            notes = "This API verifies the OTP sent to the user's mobile number. If the OTP is valid and verified, it registers the user in the system."
    )
    public ResponseEntity<ApiResponseDTO<OtpVerificationResponseDTO>> verifyMobileOtpAndRegisterUser(
            @RequestBody CustomerVerificationRequestDTO dto
    );

    @PostMapping("/send-otp-mobile")
    @ApiOperation(
            value = "Send OTP to mobile number",
            notes = "This API generates a one-time password (OTP) and sends it to the specified mobile number for verification purposes."
    )
    public ResponseEntity<ApiResponseDTO<OtpVerificationResponseDTO>> sendMobileOtp(
            @RequestParam Long mobileNumber
    );

    @PostMapping("/validate-mobile-otp")
    @ApiOperation(
            value = "Validate mobile OTP",
            notes = "This API verifies the mobile OTP entered by the user. Upon successful verification"
    )
    ResponseEntity<ApiResponseDTO<VerifiedOtpResponseDTO>> validateMobileOtp(
            @RequestBody VerifiyOtpRequestDTO verifiyOtpRequestDTO
    );

    @PostMapping("/send-otp-user-id")
    @ApiOperation(
            value = "Send OTP to mobile number by user id.",
            notes = "This API generates a one-time password (OTP) and sends it to the specified mobile number for verification purposes."
    )

    public ResponseEntity<ApiResponseDTO<OtpVerificationResponseDTO>> sendOtpByUserId(
            @RequestParam Long userId
    );

}

