package com.sipl.ticket.notificationService.service.impl;

import com.sipl.ticket.core.dao.entity.Customers;
import com.sipl.ticket.core.dao.entity.OtpVerification;
import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dao.repository.CustomerRepository;
import com.sipl.ticket.core.dao.repository.OtpVerificationRepository;
import com.sipl.ticket.core.dao.repository.UsersRepository;
import com.sipl.ticket.core.dto.request.CustomerVerificationRequestDTO;
import com.sipl.ticket.core.dto.request.UserMasterRequestDTO;
import com.sipl.ticket.core.dto.request.UserRequestDTO;
import com.sipl.ticket.core.dto.request.VerifiyOtpRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.OtpVerificationResponseDTO;
import com.sipl.ticket.core.dto.response.UserRoleApiResponse;
import com.sipl.ticket.core.dto.response.VerifiedOtpResponseDTO;
import com.sipl.ticket.notificationService.service.OtpVerificationService;
import com.sipl.notification.callback.NotificationCallback;
import com.sipl.notification.dto.request.EmailNotificationRequest;
import com.sipl.notification.dto.response.NotificationResponse;
import com.sipl.notification.enums.NotificationPriority;
import com.sipl.notification.service.impl.Notification;
import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpVerificationServiceImpl implements OtpVerificationService {

    private final UsersRepository usersRepository;
    private final Notification notification;
    private final OtpVerificationRepository otpVerificationRepository;
    private final CustomerRepository customerRepository;
    private static final int OTP_EXPIRATION_MINUTES = 10;

    @Value("${email.request.id}")
    private String requestId;

    @Value("${send.otp.template.id}")
    private Long otpTemplateId;

    @Value("${sender.mail}")
    private String senderMail;

    @Value("${save.customer.api.url}")
    private String saveCustomerApiUrl;


    @Override
    public ApiResponseDTO<OtpVerificationResponseDTO> sendOtp(String username) {
        try {
            Optional<Users> fetchedUsers = usersRepository.findByUserName(username);

            if (fetchedUsers.isEmpty()) {
                return new ApiResponseDTO<>(null, "User not found.", HttpStatus.BAD_REQUEST, true);
            }
            Users user = fetchedUsers.get();
            String emailOtp = generateOtp();
            LocalDateTime otpExpiryTime = LocalDateTime.now().plusMinutes(10);

            OtpVerification otpVerification = otpVerificationRepository
                    .findByEmailId(user.getEmailId())
                    .orElse(new OtpVerification());
            otpVerification.setEmailId(user.getEmailId());
            otpVerification.setEmailOtp(emailOtp);
            otpVerification.setIsOtpVerified(false);
            otpVerification.setValidateOtpExpiration(otpExpiryTime);
            otpVerification = otpVerificationRepository.save(otpVerification);
            sendOtpEmail(user.getEmailId(), emailOtp, requestId);
            OtpVerificationResponseDTO response = new OtpVerificationResponseDTO();
            response.setOtpVerificationId(otpVerification.getOtpVerificationId());
            response.setEmailId(otpVerification.getEmailId());
            return new ApiResponseDTO<>(response,
                    "Otp has been sent successfully to your registered email id",
                    HttpStatus.OK, false);
        } catch (IllegalArgumentException e) {
            log.error("Validation failed while sending OTP for user: {}", username, e);
            return new ApiResponseDTO<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, true);

        } catch (Exception e) {
            log.error("Unexpected error while sending OTP for user: {}", username, e);
            return new ApiResponseDTO<>(null,
                    "Something went wrong while sending OTP. Please try again.",
                    HttpStatus.INTERNAL_SERVER_ERROR, true);
        }
    }

    private void sendOtpEmail(String emailId, String emailOtp, String requestId) throws Exception {
        if (emailId == null ) {
            throw new IllegalArgumentException("Email ID must not be null or empty");
        }
        if (emailOtp == null) {
            throw new IllegalArgumentException("OTP must not be null or empty");
        }
        if (otpTemplateId == null) {
            throw new IllegalArgumentException("Template ID must not be null or empty");
        }
        if (senderMail == null) {
            throw new IllegalArgumentException("Sender mail must not be null or empty");
        }

        EmailNotificationRequest emailRequest = new EmailNotificationRequest();
        emailRequest.setTo(Collections.singletonList(emailId));
        emailRequest.setSubject("OTP VERIFICATION");
        emailRequest.setSender(senderMail);
        emailRequest.setTemplateId(otpTemplateId);
        emailRequest.setPriority(NotificationPriority.DEFAULT);
        Map<String, String> emailRequestData = new HashMap<>();
        emailRequestData.put("OTP", emailOtp);
        emailRequest.setTemplateData(emailRequestData);
        log.info("Sending OTP email to: {}", emailId);
        try {
            notification.sendEmail(
                    emailRequest,
                    requestId,
                    null,
                    new NotificationCallback() {
                        @Override
                        public void onSuccess(ResponseEntity<NotificationResponse<?>> responseEntity) {
                            String responseBody = Objects.toString(responseEntity.getBody(), "No response body");
                            log.info("Email sent successfully. Response:\n{}", responseBody);
                        }

                        @Override
                        public void onFailure(ResponseEntity<NotificationResponse<?>> responseEntity) {
                            log.error("Failed to send email. Response: {}", responseEntity);
                            throw new IllegalArgumentException("Failed to send OTP email. Subject or template may be missing.");
                        }
                    }
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateOtp() {
        return RandomStringUtils.randomNumeric(6);
    }


    @Override
    public ApiResponseDTO<VerifiedOtpResponseDTO> validateOtp(VerifiyOtpRequestDTO request) {
        try {
            OtpVerification otpEntity = otpVerificationRepository.findById(request.getOtpVerificationId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid OTP request"));

            if (isOtpExpired(otpEntity.getValidateOtpExpiration())) {
                return new ApiResponseDTO<>(null, "OTP has expired. Please request a new one.",
                        HttpStatus.BAD_REQUEST, true, null);
            }

            if (!otpEntity.getEmailOtp().equals(request.getEmailOtp())) {
                return new ApiResponseDTO<>(null, "Invalid OTP. Please try again.",
                        HttpStatus.BAD_REQUEST, true, null);
            }

            otpEntity.setIsOtpVerified(true);
            otpVerificationRepository.save(otpEntity);

            Users user = usersRepository.findByEmailId(request.getEmailId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found for the provided email."));

            VerifiedOtpResponseDTO responseDto = new VerifiedOtpResponseDTO();
            responseDto.setId(user.getId());
            responseDto.setFirstName(user.getFirstName());
            responseDto.setMiddleName(user.getMiddleName());
            responseDto.setLastName(user.getLastName());
            responseDto.setUserName(user.getUserName());

            return new ApiResponseDTO<>(responseDto, "OTP verified successfully",
                    HttpStatus.OK, false, null);

        } catch (IllegalArgumentException e) {
            // Business errors (invalid OTP, expired, user not found)
            return new ApiResponseDTO<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, true, null);

        } catch (Exception e) {
            // System errors
            log.error("Unexpected error while validating OTP", e);
            return new ApiResponseDTO<>(null,
                    "Something went wrong while validating OTP. Please try again.",
                    HttpStatus.INTERNAL_SERVER_ERROR, true, null);
        }
    }

    private boolean isOtpExpired(LocalDateTime expirationTime) {
        if (expirationTime == null) {
            return true;
        }
        return LocalDateTime.now().isAfter(expirationTime);
    }

    @Override
    public ApiResponseDTO<OtpVerificationResponseDTO> validateUserNameAndSendOtp(CustomerVerificationRequestDTO dto){
        try {
            Long mobileNo=dto.getMobileNumber();
            Optional<Users> userByUsername = usersRepository.findByUserName(dto.getUsername());
            if (userByUsername.isPresent()) {
                return new ApiResponseDTO<>(
                        null,
                        "This username is already taken. Please choose another one.",
                        HttpStatus.CONFLICT,
                        true
                );
            }
            return generateAndSendOtp(dto.getMobileNumber());
        } catch (Exception e) {
            log.error("Unexpected error while sending Mobile OTP ", e);
            return new ApiResponseDTO<>(null,
                    "Something went wrong while sending OTP. Please try again.",
                    HttpStatus.INTERNAL_SERVER_ERROR, true);
        }
    }

    @Override
    public ApiResponseDTO<OtpVerificationResponseDTO> verifyMobileOtpAndRegisterUser(CustomerVerificationRequestDTO dto) {
        try {
            OtpVerification otpEntity = validateOtpCommon(dto.getOtpVerificationId(), dto.getMobileOtp());
            Optional<Customers> fetchedCustomerByMobileNo = customerRepository.findByContactNo(dto.getMobileNumber());
            if (fetchedCustomerByMobileNo.isEmpty()) {
                log.info("Customer not found for mobile number: {}", dto.getMobileNumber());
                return new ApiResponseDTO<>(null, "Customer not found", HttpStatus.NOT_FOUND, true, null);
            }

            Customers customers = fetchedCustomerByMobileNo.get();
            UserRequestDTO userRequestDTO = new UserRequestDTO();
            UserMasterRequestDTO userMasterRequestDTO = new UserMasterRequestDTO();
            userMasterRequestDTO.setUserName(dto.getUsername());
            userMasterRequestDTO.setPassword(dto.getPassword());
            userMasterRequestDTO.setFirstName(customers.getCustomerName());
            if (dto.getEmail() != null) {
                userMasterRequestDTO.setEmailId(dto.getEmail());
            }
            userMasterRequestDTO.setPhoneNumber(dto.getMobileNumber().toString());
            userRequestDTO.setUserMasterRequestDTO(userMasterRequestDTO);
            userRequestDTO.setCreatedBy(dto.getUsername());
            log.info("Registering user via API: {}", saveCustomerApiUrl);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<UserRoleApiResponse> responseEntity =
                    restTemplate.postForEntity(saveCustomerApiUrl, userRequestDTO, UserRoleApiResponse.class);
            UserRoleApiResponse userRoleResponse = responseEntity.getBody();
            if (userRoleResponse.isError()) {
                return new ApiResponseDTO<>(null, userRoleResponse.getMessage(),
                        userRoleResponse.getStatus() != null ? userRoleResponse.getStatus() : HttpStatus.BAD_REQUEST,
                        true, null);
            }
            return new ApiResponseDTO<>(null,
                    "OTP verification completed and user registration successful.",
                    HttpStatus.OK, false, null);
        } catch (IllegalArgumentException e) {
            return new ApiResponseDTO<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, true, null);
        } catch (Exception e) {
            log.error("Unexpected error while validating OTP", e);
            return new ApiResponseDTO<>(null,
                    "Something went wrong while validating OTP. Please try again.",
                    HttpStatus.INTERNAL_SERVER_ERROR, true, null);
        }
    }

    @Override
    public ApiResponseDTO<OtpVerificationResponseDTO> sendMobileOtp(Long mobileNumber) {
        return generateAndSendOtp(mobileNumber);
    }

    private ApiResponseDTO<OtpVerificationResponseDTO> generateAndSendOtp(Long mobileNumber) {
        try {
            Optional<Users> getUserDetails = usersRepository.findByMobileNumber(mobileNumber.toString());
            if (getUserDetails.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "This mobile number does not exist in our system. Please contact the administrator.",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }
            String mobileOtp = generateOtp();
            LocalDateTime otpExpiryTime = LocalDateTime.now().plusMinutes(10);

            OtpVerification otpVerification = otpVerificationRepository
                    .findByMobileNumber(mobileNumber)
                    .orElse(new OtpVerification());

            otpVerification.setMobileNumber(mobileNumber);
            otpVerification.setMobileOtp(mobileOtp);
            otpVerification.setIsOtpVerified(false);
            otpVerification.setValidateOtpExpiration(otpExpiryTime);
            otpVerification = otpVerificationRepository.save(otpVerification);
            // TODO: send Mobile OTP logic
            // sendOtpEmail(mobileNumber, mobileOtp);
            OtpVerificationResponseDTO response = new OtpVerificationResponseDTO();
            response.setOtpVerificationId(otpVerification.getOtpVerificationId());
            response.setMobileNumber(otpVerification.getMobileNumber());

            return new ApiResponseDTO<>(response,
                    "Otp has been sent successfully to your registered mobile number",
                    HttpStatus.OK, false);

        } catch (Exception e) {
            log.error("Unexpected error while sending Mobile OTP ", e);
            return new ApiResponseDTO<>(null,
                    "Something went wrong while sending OTP. Please try again.",
                    HttpStatus.INTERNAL_SERVER_ERROR, true);
        }
    }

    @Override
    public ApiResponseDTO<VerifiedOtpResponseDTO> validateMobileOtp(VerifiyOtpRequestDTO verifiyOtpRequestDTO) {
        try {
            OtpVerification otpEntity = validateOtpCommon(verifiyOtpRequestDTO.getOtpVerificationId(), verifiyOtpRequestDTO.getMobileOtp());
            Users user = usersRepository.findByMobileNumber(verifiyOtpRequestDTO.getMobileNumber().toString())
                    .orElseThrow(() -> new IllegalArgumentException("User not found for the provided mobile number."));
            return new ApiResponseDTO<>(null, "OTP verified successfully",
                    HttpStatus.OK, false, null);
        } catch (IllegalArgumentException e) {
            return new ApiResponseDTO<>(null, e.getMessage(), HttpStatus.BAD_REQUEST, true, null);
        } catch (Exception e) {
            log.error("Unexpected error while validating OTP", e);
            return new ApiResponseDTO<>(null,
                    "Something went wrong while validating OTP. Please try again.",
                    HttpStatus.INTERNAL_SERVER_ERROR, true, null);
        }
    }

    private OtpVerification validateOtpCommon(Long otpVerificationId, String providedOtp) {
        OtpVerification otpEntity = otpVerificationRepository.findById(otpVerificationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid OTP request"));

        if (isOtpExpired(otpEntity.getValidateOtpExpiration())) {
            throw new IllegalArgumentException("OTP has expired. Please request a new one.");
        }

        if (!otpEntity.getMobileOtp().equals(providedOtp)) {
            throw new IllegalArgumentException("Invalid OTP. Please try again.");
        }

        otpEntity.setIsOtpVerified(true);
        otpVerificationRepository.save(otpEntity);
        return otpEntity;
    }

    @Override
    public ApiResponseDTO<OtpVerificationResponseDTO> sendOtpByUserId(Long userId) {

        Optional<Users> fetchedUsers = usersRepository.findById(userId);

        if (fetchedUsers.isEmpty()) {
            return new ApiResponseDTO<>(null, "User not found.", HttpStatus.BAD_REQUEST, true);
        }

        String phoneNumber=fetchedUsers.get().getPhoneNumber();
        Long mobileNumber=Long.parseLong(phoneNumber);
        return generateAndSendOtp(mobileNumber);
    }

}
