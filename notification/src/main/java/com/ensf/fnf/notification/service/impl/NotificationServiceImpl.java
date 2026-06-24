package com.ensf.fnf.notification.service.impl;

import com.ensf.fnf.core.dao.entity.UserEntity;
import com.ensf.fnf.helper.EmailHelper;
import com.ensf.fnf.helper.SmsHelper;
import com.ensf.fnf.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl
        implements NotificationService {

    private final EmailHelper emailHelper;

    private final SmsHelper smsHelper;

    @Override
    public void sendAccountCreatedNotification(
            UserEntity user) {

        log.info(
                "<<START>> sendAccountCreatedNotification <<START>>"
        );

        try {

            sendWelcomeEmail(
                    user.getEmailAddress(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getMobileNumber());

            sendWelcomeSms(
                    user.getMobileNumber(),
                    user.getFirstName(),
                    user.getLastName()

            );

            log.info(
                    "Account created notification sent successfully. userId={}",
                    user.getUserId()
            );

        } catch (Exception ex) {

            log.error(
                    "Error while sending account created notification. userId={}",
                    user.getUserId(),
                    ex
            );

            throw new RuntimeException(
                    "Failed to send account created notification",
                    ex
            );
        }

        log.info(
                "<<END>> sendAccountCreatedNotification <<END>>"
        );
    }

    @Override
    public void sendWelcomeEmail(
            String email,
            String firstName,
            String lastName,
            String mobileNumber) {

        log.info(
                "<<START>> sendWelcomeEmail service <<START>>"
        );

        try {

            emailHelper.sendWelcomeEmail(
                    email,
                    firstName,
                    lastName,
                    mobileNumber
            );

            log.info(
                    "Welcome Email sent successfully. email={}",
                    email
            );

        } catch (Exception ex) {

            log.error(
                    "Error while sending Welcome Email. email={}",
                    email,
                    ex
            );

            throw ex;
        }

        log.info(
                "<<END>> sendWelcomeEmail service <<END>>"
        );
    }

    @Override
    public void sendWelcomeSms(
            String mobileNumber,
            String firstName,
            String lastName) {

        log.info(
                "<<START>> sendWelcomeSms service <<START>>"
        );

        try {

            smsHelper.sendWelcomeSms(
                    mobileNumber,
                    firstName,
                    lastName
            );

            log.info(
                    "Welcome SMS sent successfully. mobileNumber={}",
                    mobileNumber
            );

        } catch (Exception ex) {

            log.error(
                    "Error while sending Welcome SMS. mobileNumber={}",
                    mobileNumber,
                    ex
            );

            throw ex;
        }

        log.info(
                "<<END>> sendWelcomeSms service <<END>>"
        );
    }

    @Override
    public void sendOtpEmail(
            String email,
            String otp) {

        log.info(
                "<<START>> sendOtpEmail service <<START>>"
        );

        try {

            emailHelper.sendOtpEmail(
                    email,
                    otp
            );

            log.info(
                    "OTP Email sent successfully. email={}",
                    email
            );

        } catch (Exception ex) {

            log.error(
                    "Error while sending OTP Email. email={}",
                    email,
                    ex
            );

            throw ex;
        }

        log.info(
                "<<END>> sendOtpEmail service <<END>>"
        );
    }

    @Override
    public void sendOtpSms(
            String mobileNumber,
            String otp) {

        log.info(
                "<<START>> sendOtpSms service <<START>>"
        );

        try {

            smsHelper.sendOtpSms(
                    mobileNumber,
                    otp
            );

            log.info(
                    "OTP SMS sent successfully. mobileNumber={}",
                    mobileNumber
            );

        } catch (Exception ex) {

            log.error(
                    "Error while sending OTP SMS. mobileNumber={}",
                    mobileNumber,
                    ex
            );

            throw ex;
        }

        log.info(
                "<<END>> sendOtpSms service <<END>>"
        );
    }
}