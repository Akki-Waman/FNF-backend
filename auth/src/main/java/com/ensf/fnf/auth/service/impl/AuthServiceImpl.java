package com.ensf.fnf.auth.service.impl;

import com.ensf.fnf.auth.service.AuthService;
import com.ensf.fnf.core.dao.entity.OtpEntity;
import com.ensf.fnf.core.dao.entity.UserEntity;
import com.ensf.fnf.core.dao.repository.OtpRepository;
import com.ensf.fnf.core.dao.repository.UserRepository;
import com.ensf.fnf.core.dto.UserLoginView;
import com.ensf.fnf.core.dto.requestDto.CreateAccountRequestDto;
import com.ensf.fnf.core.dto.requestDto.LoginRequestDto;
import com.ensf.fnf.core.dto.requestDto.SendOtpRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.LoginResponse;
import com.ensf.fnf.core.mapper.UserMapper;
import com.ensf.fnf.core.util.JwtUtil;
import com.ensf.fnf.core.util.OtpUtil;
import com.ensf.fnf.facade.NotificationFacade;
import com.ensf.fnf.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final UserMapper userMapper;
    private final NotificationService notificationService;
    private final NotificationFacade notificationFacade;
    private final JwtUtil jwtUtil;

    @Override
    public CommonApiResponse<LoginResponse>
    createAccount(
            CreateAccountRequestDto dto) {

        log.info(
                "<<START>> createAccount service <<START>>"
        );

        validateCreateAccountRequest(dto);

        validateUserAlreadyExists(dto);

        UserEntity userEntity =
                buildUserEntity(dto);

        UserEntity savedUser =
                saveUser(userEntity);

        sendWelcomeEmail(savedUser);

        sendWelcomeSms(savedUser);

        LoginResponse responseDto =
                buildCreateAccountResponse(savedUser);

        log.info(
                "<<END>> createAccount service <<END>>"
        );

        return CommonApiResponse
                .<LoginResponse>builder()
                .success(true)
                .message("Account created successfully")
                .data(responseDto)
                .build();
    }

    private void validateCreateAccountRequest(
            CreateAccountRequestDto dto) {

        log.info(
                "<<START>> validateCreateAccountRequest <<START>>"
        );

        if (dto.getFullName() == null
                || dto.getFullName().trim().isEmpty()) {

            throw new ValidationException(
                    "Full name is required"
            );
        }

        if (dto.getMobileNumber() == null
                || dto.getMobileNumber().trim().isEmpty()) {

            throw new ValidationException(
                    "Mobile number is required"
            );
        }

        if (dto.getEmail() == null
                || dto.getEmail().trim().isEmpty()) {

            throw new ValidationException(
                    "Email is required"
            );
        }

        log.info(
                "<<END>> validateCreateAccountRequest <<END>>"
        );
    }

    private void validateUserAlreadyExists(
            CreateAccountRequestDto dto) {

        log.info(
                "<<START>> validateUserAlreadyExists <<START>>"
        );

        boolean mobileExists =
                userRepository
                        .existsByMobileNumber(
                                dto.getMobileNumber());

        if (mobileExists) {

            throw new ValidationException(
                    "User already exists with mobile number"
            );
        }

        boolean emailExists =
                userRepository
                        .existsByEmail(
                                dto.getEmail());

        if (emailExists) {

            throw new ValidationException(
                    "User already exists with email"
            );
        }

        log.info(
                "<<END>> validateUserAlreadyExists <<END>>"
        );
    }

    private UserEntity buildUserEntity(
            CreateAccountRequestDto dto) {

        log.info(
                "<<START>> buildUserEntity <<START>>"
        );

        UserEntity entity =
                UserEntity.builder()
                        .fullName(
                                dto.getFullName().trim()
                        )
                        .email(
                                dto.getEmail().trim()
                        )
                        .mobileNumber(
                                dto.getMobileNumber().trim()
                        )
                        .gender(
                                dto.getGender()
                        )
                        .verified(false)
                        .build();

        log.info(
                "User Entity Built => fullName={}, email={}, mobileNumber={}",
                entity.getFullName(),
                entity.getEmail(),
                entity.getMobileNumber()
        );

        log.info(
                "<<END>> buildUserEntity <<END>>"
        );

        return entity;
    }

    private UserEntity saveUser(
            UserEntity userEntity) {

        log.info(
                "Before Save => fullName={}, email={}, mobileNumber={}",
                userEntity.getFullName(),
                userEntity.getEmail(),
                userEntity.getMobileNumber()
        );

        return userRepository.save(userEntity);
    }

    private void sendWelcomeEmail(
            UserEntity user) {

        log.info(
                "<<START>> sendWelcomeEmail <<START>>"
        );

        notificationService.sendWelcomeEmail(
                user.getEmail(),
                user.getFullName(),
                user.getMobileNumber()
        );

        log.info(
                "<<END>> sendWelcomeEmail <<END>>"
        );
    }

    private void sendWelcomeSms(
            UserEntity user) {

        log.info(
                "<<START>> sendWelcomeSms <<START>>"
        );

        notificationService.sendWelcomeSms(
                user.getMobileNumber(),
                user.getFullName()
        );

        log.info(
                "<<END>> sendWelcomeSms <<END>>"
        );
    }

    private LoginResponse buildCreateAccountResponse(
            UserEntity user) {

        log.info(
                "<<START>> buildCreateAccountResponse <<START>>"
        );

        UserLoginView userView =
                UserLoginView.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .mobileNumber(
                                user.getMobileNumber()
                        )
                        .gender(user.getGender())
                        .profileComplete(user.isProfileComplete())
                        .build();

        LoginResponse response =
                LoginResponse.builder()
                        .loginSuccess(true)
                        .bearerToken(null)
                        .user(userView)
                        .build();

        log.info(
                "<<END>> buildCreateAccountResponse <<END>>"
        );

        return response;
    }
    private void sendWelcomeNotification(
            UserEntity user) {

        notificationFacade.sendAccountCreatedNotification(
                user
        );
    }

    @Override
    public CommonApiResponse<String> sendOtp(
            SendOtpRequestDto dto) {

        log.info(
                "<<START>> sendOtp Service <<START>>"
        );

        UserEntity user =
                getUser(dto);

        String otp =
                generateOtp();

        saveOtp(
                user,
                otp
        );

        sendOtpNotification(
                user,
                otp
        );

        log.info(
                "<<END>> sendOtp Service <<END>>"
        );

        return CommonApiResponse.<String>builder()
                .success(true)
                .message("OTP sent successfully")
                .data(null)
                .build();
    }

    private UserEntity getUser(
            SendOtpRequestDto dto) {

        log.info(
                "<<START>> getUser <<START>>"
        );

        UserEntity user;

        if (dto.getMobileNumber() != null &&
                !dto.getMobileNumber().trim().isEmpty()) {

            user = userRepository
                    .findByMobileNumber(
                            dto.getMobileNumber()
                    )
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "User not found"
                            )
                    );

        } else {

            user = userRepository
                    .findByEmail(
                            dto.getEmail()
                    )
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "User not found"
                            )
                    );
        }

        log.info(
                "<<END>> getUser <<END>>"
        );

        return user;
    }

    private String generateOtp() {

        log.info(
                "<<START>> generateOtp <<START>>"
        );

        String otp =
                OtpUtil.generateOtp();

        log.info(
                "<<END>> generateOtp <<END>>"
        );

        return otp;
    }

    private void saveOtp(
            UserEntity user,
            String otp) {

        log.info(
                "<<START>> saveOtp <<START>>"
        );

        OtpEntity entity =
                OtpEntity.builder()
                        .email(user.getEmail())
                        .mobileNumber(user.getMobileNumber())
                        .otp(otp)
                        .verified(false)
                        .attemptCount(0)
                        .expiryTime(
                                LocalDateTime.now()
                                        .plusMinutes(5)
                        )
                        .build();

        otpRepository.save(
                entity
        );

        log.info(
                "<<END>> saveOtp <<END>>"
        );
    }

    private void sendOtpNotification(
            UserEntity user,
            String otp) {

        log.info(
                "<<START>> sendOtpNotification <<START>>"
        );

        notificationFacade
                .sendOtpNotification(
                        user,
                        otp
                );

        log.info(
                "<<END>> sendOtpNotification <<END>>"
        );
    }

    @Override
    public CommonApiResponse<LoginResponse> login(
            LoginRequestDto dto) {

        log.info(
                "<<START>> login <<START>>"
        );

        UserEntity user =
                getLoginUser(dto);

        OtpEntity otpEntity =
                validateOtp(
                        user,
                        dto.getOtp()
                );

        markOtpAsVerified(
                otpEntity
        );

        String token =
                generateJwtToken(
                        user
                );

        LoginResponse response =
                buildLoginResponse(
                        user,
                        token
                );

        log.info(
                "<<END>> login <<END>>"
        );

        return CommonApiResponse
                .<LoginResponse>builder()
                .success(true)
                .message("Login successful")
                .data(response)
                .build();
    }
    private UserEntity getLoginUser(
            LoginRequestDto dto) {

        log.info(
                "<<START>> getLoginUser <<START>>"
        );

        UserEntity user;

        if (dto.getMobileNumber() != null
                && !dto.getMobileNumber().trim().isEmpty()) {

            user = userRepository
                    .findByMobileNumber(
                            dto.getMobileNumber()
                    )
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "User not found"
                            )
                    );

        } else {

            user = userRepository
                    .findByEmail(
                            dto.getEmail()
                    )
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "User not found"
                            )
                    );
        }

        log.info(
                "<<END>> getLoginUser <<END>>"
        );

        return user;
    }

    private OtpEntity validateOtp(
            UserEntity user,
            String otp) {

        log.info(
                "<<START>> validateOtp <<START>>"
        );

        OtpEntity otpEntity =
                otpRepository
                        .validateOtp(
                                user.getMobileNumber(),
                                otp
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Invalid OTP"
                                )
                        );

        if (Boolean.TRUE.equals(
                otpEntity.isVerified()
        )) {

            throw new RuntimeException(
                    "OTP already used"
            );
        }

        if (otpEntity
                .getExpiryTime()
                .isBefore(
                        LocalDateTime.now()
                )) {

            throw new RuntimeException(
                    "OTP expired"
            );
        }

        log.info(
                "<<END>> validateOtp <<END>>"
        );

        return otpEntity;
    }
    private void markOtpAsVerified(
            OtpEntity otpEntity) {

        log.info(
                "<<START>> markOtpAsVerified <<START>>"
        );

        otpEntity.setVerified(
                true
        );

        otpRepository.save(
                otpEntity
        );

        log.info(
                "<<END>> markOtpAsVerified <<END>>"
        );
    }
    private String generateJwtToken(
            UserEntity user) {

        log.info(
                "<<START>> generateJwtToken <<START>>"
        );

        String token =
                jwtUtil.generateToken(
                        user.getEmail()
                );

        log.info(
                "<<END>> generateJwtToken <<END>>"
        );

        return token;
    }
    private LoginResponse buildLoginResponse(
            UserEntity user,
            String token) {

        log.info(
                "<<START>> buildLoginResponse <<START>>"
        );

        UserLoginView userView =
                UserLoginView.builder()
                        .id(
                                user.getId()
                        )
                        .fullName(
                                user.getFullName()
                        )
                        .email(
                                user.getEmail()
                        )
                        .mobileNumber(
                                user.getMobileNumber()
                        )
                        .gender(
                                user.getGender()
                        )
                        .profileComplete(
                                user.isProfileComplete()
                        )
                        .build();

        LoginResponse response =
                LoginResponse.builder()
                        .loginSuccess(
                                true
                        )
                        .bearerToken(
                                "Bearer " + token
                        )
                        .user(
                                userView
                        )
                        .build();

        log.info(
                "<<END>> buildLoginResponse <<END>>"
        );

        return response;
    }
}