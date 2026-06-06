package com.ensf.fnf.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsHelper {

    public void sendWelcomeSms(
            String mobileNumber,
            String fullName) {

        log.info(
                "<<START>> sendWelcomeSms <<START>>"
        );

        try {

            String message =
                    "Hello " + fullName +
                            ", your FNF account has been created successfully.";

            /*
             MSG91 / TWILIO API CALL HERE
             */

            log.info(
                    "Welcome SMS sent successfully. mobileNumber={}, message={}",
                    mobileNumber,
                    message
            );

        } catch (Exception ex) {

            log.error(
                    "Failed to send welcome SMS. mobileNumber={}",
                    mobileNumber,
                    ex
            );

            throw new RuntimeException(
                    "Failed to send welcome SMS"
            );
        }

        log.info(
                "<<END>> sendWelcomeSms <<END>>"
        );
    }

    public void sendOtpSms(
            String mobileNumber,
            String otp) {

        log.info(
                "<<START>> sendOtpSms <<START>>"
        );

        try {

            String message =
                    "Your FNF OTP is : " + otp;

            /*
             MSG91 / TWILIO API CALL HERE
             */

            log.info(
                    "OTP SMS sent successfully. mobileNumber={}, message={}",
                    mobileNumber,
                    message
            );

        } catch (Exception ex) {

            log.error(
                    "Failed to send OTP SMS. mobileNumber={}",
                    mobileNumber,
                    ex
            );

            throw new RuntimeException(
                    "Failed to send OTP SMS"
            );
        }

        log.info(
                "<<END>> sendOtpSms <<END>>"
        );
    }
}