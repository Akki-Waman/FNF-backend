package com.ensf.fnf.facade.impl;

import com.ensf.fnf.core.dao.entity.UserEntity;
import com.ensf.fnf.facade.NotificationFacade;
import com.ensf.fnf.helper.EmailHelper;
import com.ensf.fnf.helper.SmsHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationFacadeImpl
        implements NotificationFacade {

    private final EmailHelper emailHelper;

    private final SmsHelper smsHelper;

    @Override
    public void sendAccountCreatedNotification(
            UserEntity user) {

        log.info(
                "<<START>> sendAccountCreatedNotification <<START>>"
        );

        try {

            emailHelper.sendWelcomeEmail(
                    user.getEmail(),
                    user.getFullName(),
                    user.getMobileNumber()
            );

        } catch (Exception ex) {

            log.error(
                    "Welcome email failed. userId={}",
                    user.getId(),
                    ex
            );
        }

        try {

            smsHelper.sendWelcomeSms(
                    user.getMobileNumber(),
                    user.getFullName()
            );

        } catch (Exception ex) {

            log.error(
                    "Welcome SMS failed. userId={}",
                    user.getId(),
                    ex
            );
        }

        log.info(
                "Account created notification processed successfully. userId={}",
                user.getId()
        );

        log.info(
                "<<END>> sendAccountCreatedNotification <<END>>"
        );
    }

    @Override
    public void sendOtpNotification(
            UserEntity user,
            String otp) {

        log.info(
                "<<START>> sendOtpNotification <<START>>"
        );

        try {

            emailHelper.sendOtpEmail(
                    user.getEmail(),
                    otp
            );

        } catch (Exception ex) {

            log.error(
                    "OTP email failed. userId={}",
                    user.getId(),
                    ex
            );
        }

        try {

            smsHelper.sendOtpSms(
                    user.getMobileNumber(),
                    otp
            );

        } catch (Exception ex) {

            log.error(
                    "OTP SMS failed. userId={}",
                    user.getId(),
                    ex
            );
        }

        log.info(
                "OTP notification processed successfully. userId={}",
                user.getId()
        );

        log.info(
                "<<END>> sendOtpNotification <<END>>"
        );
    }
}