package com.ensf.fnf.auth.service.impl;

import com.ensf.fnf.auth.service.AuthService;
import com.ensf.fnf.core.dao.entity.OtpVerificationEntity;
import com.ensf.fnf.core.dao.entity.UserEntity;
import com.ensf.fnf.core.dao.repository.OtpVerificationRepository;
import com.ensf.fnf.core.dao.repository.UserRepository;
import com.ensf.fnf.core.dto.requestDto.CreateProfileRequestDto;
import com.ensf.fnf.core.dto.requestDto.SendOtpRequestDto;
import com.ensf.fnf.core.dto.requestDto.VerifyOtpRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.LoginResponseDto;
import com.ensf.fnf.core.enums.AccountStatus;
import com.ensf.fnf.core.enums.OtpPurpose;
import com.ensf.fnf.core.exception.InvalidOtpException;
import com.ensf.fnf.core.mapper.UserMapper;
import com.ensf.fnf.core.security.JwtTokenHelper;
import com.ensf.fnf.facade.NotificationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl
        implements AuthService {

    private final UserRepository userRepository;

    private final OtpVerificationRepository otpVerificationRepository;

    private final JwtTokenHelper jwtTokenHelper;

    private final NotificationFacade notificationFacade;

    private final UserMapper userMapper;

    private static final int OTP_EXPIRY_MINUTES = 5;


    @Override
    public CommonApiResponse<String>
    sendOtp(
            SendOtpRequestDto dto) {

        log.info(
                "AuthServiceImpl :: sendOtp :: START"
        );

        validateSendOtpRequest(
                dto
        );

        String otp =
                generateOtp();

        saveOtp(
                dto.getUsername(),
                otp
        );

        sendOtpNotification(
                dto.getUsername(),
                otp
        );

        log.info(
                "OTP sent successfully. username={}",
                dto.getUsername()
        );

        log.info(
                "AuthServiceImpl :: sendOtp :: END"
        );

        return CommonApiResponse
                .<String>builder()
                .success(
                        true
                )
                .message(
                        "OTP sent successfully"
                )
                .build();

    }
    private void validateSendOtpRequest(
            SendOtpRequestDto dto) {

        if (dto == null) {

            log.error(
                    "Send OTP request is null"
            );

            throw new RuntimeException(
                    "Request cannot be null"
            );
        }

        if (dto.getUsername() == null
                || dto.getUsername()
                .trim()
                .isEmpty()) {

            log.error(
                    "Username is required"
            );

            throw new RuntimeException(
                    "Username is required"
            );
        }

    }

    @Override
    public CommonApiResponse<LoginResponseDto> verifyOtp(
            VerifyOtpRequestDto dto) {

        log.info(
                "AuthServiceImpl :: verifyOtp :: START"
        );

        validateVerifyOtpRequest(
                dto
        );

        OtpVerificationEntity otpEntity =
                getLatestOtp(
                        dto.getUsername()
                );

        validateOtp(
                dto,
                otpEntity
        );

        UserEntity user =
                getOrCreateUser(
                        dto.getUsername()
                );

        otpEntity.setVerified(
                true
        );

        otpVerificationRepository.save(
                otpEntity
        );

        String token =
                jwtTokenHelper.generateToken(
                        dto.getUsername()
                );

        LoginResponseDto response =
                LoginResponseDto.builder()
                        .userId(
                                user.getUserId()
                        )
                        .jwtToken(
                                token
                        )
                        .profileCompleted(
                                Boolean.TRUE.equals(
                                        user.getProfileCompleted()
                                )
                        )
                        .build();

        log.info(
                "AuthServiceImpl :: verifyOtp :: END"
        );

        return CommonApiResponse
                .<LoginResponseDto>builder()
                .success(
                        true
                )
                .message(
                        "Login successful"
                )
                .data(
                        response
                )
                .build();
    }

    @Override
    public CommonApiResponse<String> createProfile(
            CreateProfileRequestDto dto) {

        log.info(
                "AuthServiceImpl :: createProfile :: START"
        );

        UserEntity user =
                getLoggedInUser();

        user.setFirstName(
                dto.getFirstName()
        );

        user.setLastName(
                dto.getLastName()
        );

        user.setGender(
                dto.getGender()
        );

        user.setDateOfBirth(
                dto.getDateOfBirth()
        );

        user.setProfilePhotoUrl(
                dto.getProfilePhotoUrl()
        );

        user.setEmailVerified(
                true
        );

        user.setProfileCompleted(
                true
        );

        user.setAccountStatus(
                AccountStatus.ACTIVE
        );

        user.setActive(
                true
        );

        userRepository.save(
                user
        );

        notificationFacade
                .sendAccountCreatedNotification(
                        user
                );

        return CommonApiResponse
                .<String>builder()
                .success(
                        true
                )
                .message(
                        "Profile created successfully"
                )
                .build();
    }

    private void validateLoginRequest(
            SendOtpRequestDto dto) {

        if (dto == null
                || dto.getUsername() == null
                || dto.getUsername()
                .trim()
                .isEmpty()) {

            log.error(
                    "Username is required"
            );

            throw new RuntimeException(
                    "Username is required"
            );
        }
    }

    private void validateVerifyOtpRequest(
            VerifyOtpRequestDto dto) {

        if (dto == null) {

            throw new RuntimeException(
                    "Request cannot be null"
            );
        }

        if (dto.getUsername() == null
                || dto.getUsername()
                .trim()
                .isEmpty()) {

            throw new RuntimeException(
                    "Username is required"
            );
        }

        if (dto.getOtp() == null
                || dto.getOtp()
                .trim()
                .isEmpty()) {

            throw new RuntimeException(
                    "OTP is required"
            );
        }
    }

    private String generateOtp() {

        return String.valueOf(
                100000
                        + new Random()
                        .nextInt(
                                900000
                        )
        );
    }

    private void saveOtp(
            String username,
            String otp) {

        OtpVerificationEntity entity =
                OtpVerificationEntity
                        .builder()
                        .username(
                                username
                        )
                        .otpCode(
                                otp
                        )
                        .otpPurpose(
                                OtpPurpose.LOGIN
                        )
                        .verified(
                                false
                        )
                        .expiryTime(
                                LocalDateTime.now()
                                        .plusMinutes(
                                                OTP_EXPIRY_MINUTES
                                        )
                        )
                        .build();

        otpVerificationRepository.save(
                entity
        );
    }

    private OtpVerificationEntity getLatestOtp(
            String username) {

        List<OtpVerificationEntity> otpList =
                otpVerificationRepository
                        .findActiveOtpList(
                                username
                        );

        return otpList.stream()
                .findFirst()
                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        "OTP not found"
                                )
                );
    }

    private void validateOtp(
            VerifyOtpRequestDto dto,
            OtpVerificationEntity otpEntity) {

        if (!otpEntity.getOtpCode()
                .equals(
                        dto.getOtp()
                )) {

            log.warn(
                    "Invalid OTP for username {}",
                    dto.getUsername()
            );

            throw new InvalidOtpException(
        "Invalid OTP"
);
        }

        if (Boolean.TRUE.equals(
                otpEntity.getVerified()
        )) {

            throw new RuntimeException(
                    "OTP already verified"
            );
        }

        if (otpEntity.getExpiryTime()
                .isBefore(
                        LocalDateTime.now()
                )) {

            throw new RuntimeException(
                    "OTP expired"
            );
        }
    }

    private UserEntity getOrCreateUser(
            String username) {

        Optional<UserEntity> userOptional =
                userRepository
                        .findByUsername(
                                username
                        );

        return userOptional.orElseGet(
                () ->
                        createNewUser(
                                username
                        )
        );
    }

    private UserEntity createNewUser(
            String username) {

        UserEntity user =
                UserEntity.builder()
                        .emailAddress(
                                isEmail(
                                        username
                                )
                                        ? username
                                        : null
                        )
                        .mobileNumber(
                                isMobile(
                                        username
                                )
                                        ? username
                                        : null
                        )
                        .profileCompleted(
                                false
                        )
                        .mobileVerified(
                                false
                        )
                        .emailVerified(
                                false
                        )
                        .active(
                                true
                        )
                        .accountStatus(
                                AccountStatus.ACTIVE
                        )
                        .build();

        return userRepository.save(
                user
        );
    }

    private void sendOtpNotification(
            String username,
            String otp) {

        UserEntity user =
                UserEntity.builder()
                        .emailAddress(
                                isEmail(
                                        username
                                )
                                        ? username
                                        : null
                        )
                        .mobileNumber(
                                isMobile(
                                        username
                                )
                                        ? username
                                        : null
                        )
                        .build();

        notificationFacade
                .sendOtpNotification(
                        user,
                        otp
                );
    }

    private UserEntity getLoggedInUser() {

        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository
                .findByUsername(
                        username
                )
                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        "User not found"
                                )
                );
    }

    private boolean isEmail(
            String username) {

        return username.contains(
                "@"
        );
    }

    private boolean isMobile(
            String username) {

        return username.matches(
                "^[0-9]{10}$"
        );
    }

    @Override
    @Transactional
    public CommonApiResponse<LoginResponseDto> processOAuthLogin(com.ensf.fnf.core.dto.requestDto.OAuthLoginRequestDto dto) {
        log.info("Processing OAuth login for provider: {}", dto.getProvider());

        // 1. Verify token with Google/Apple (Mocked for now)
        validateOAuthToken(dto.getProvider(), dto.getProviderToken());

        // 2. Load or Create User
        UserEntity user = resolveOAuthUser(dto);

        // 3. Generate JWT
        String token = jwtTokenHelper.generateToken(user.getEmailAddress() != null ? user.getEmailAddress() : user.getMobileNumber());

        LoginResponseDto response = LoginResponseDto.builder()
                .userId(user.getUserId())
                .jwtToken(token)
                .profileCompleted(Boolean.TRUE.equals(user.getProfileCompleted()))
                .build();

        return CommonApiResponse.<LoginResponseDto>builder().success(true).message("OAuth Login Successful").data(response).build();
    }

    // --- Private Helpers ---
    private void validateOAuthToken(String provider, String token) {
        log.debug("Validating {} token signature...", provider);
        // Implement GoogleIdTokenVerifier logic here
    }

    private UserEntity resolveOAuthUser(com.ensf.fnf.core.dto.requestDto.OAuthLoginRequestDto dto) {
        return userRepository.findByEmailAddress(dto.getEmail()).orElseGet(() -> {
            log.info("Creating new user from OAuth payload: {}", dto.getEmail());
            UserEntity newUser = UserEntity.builder()
                    .emailAddress(dto.getEmail())
                    .firstName(dto.getFullName())
                    .emailVerified(true)
                    .mobileVerified(false)
                    .profileCompleted(false)
                    .active(true)
                    .build();
            return userRepository.save(newUser);
        });
    }
}