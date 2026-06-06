package com.ensf.fnf.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailHelper {

    private final JavaMailSender javaMailSender;

    public void sendWelcomeEmail(
            String email,
            String fullName,
            String mobileNumber) {

        log.info(
                "<<START>> sendWelcomeEmail <<START>>"
        );

        try {

            MimeMessage mimeMessage =
                    javaMailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            mimeMessage,
                            true,
                            "UTF-8"
                    );

            helper.setTo(email);

            helper.setSubject(
                    "Welcome To FNF"
            );

            String html =
                    buildWelcomeTemplate(
                            fullName,
                            email,
                            mobileNumber
                    );

            helper.setText(
                    html,
                    true
            );

            javaMailSender.send(
                    mimeMessage
            );

            log.info(
                    "Welcome email sent successfully. email={}",
                    email
            );

        } catch (Exception ex) {

            log.error(
                    "Failed to send welcome email. email={}",
                    email,
                    ex
            );

            throw new RuntimeException(
                    "Failed to send welcome email"
            );
        }

        log.info(
                "<<END>> sendWelcomeEmail <<END>>"
        );
    }

    private String buildWelcomeTemplate(
            String fullName,
            String email,
            String mobileNumber) {

        return "<html>"
                + "<body style='font-family:Arial;'>"
                + "<div style='background:#2563eb;padding:30px;color:white;text-align:center;'>"
                + "<h1>Welcome To FNF</h1>"
                + "<p>Family & Friends Reminder System</p>"
                + "</div>"

                + "<div style='padding:30px;'>"

                + "<p>Dear <b>"
                + fullName
                + "</b>,</p>"

                + "<p>Your account has been successfully created.</p>"

                + "<div style='background:#f4f4f4;"
                + "padding:20px;"
                + "border-left:5px solid #2563eb;"
                + "border-radius:8px;'>"

                + "<p><b>Email :</b> "
                + email
                + "</p>"

                + "<p><b>Mobile Number :</b> "
                + mobileNumber
                + "</p>"

                + "</div>"

                + "<p style='margin-top:20px;'>"
                + "You can now manage Birthday and Anniversary reminders."
                + "</p>"

                + "</div>"

                + "<div style='background:#111827;"
                + "padding:15px;"
                + "color:white;"
                + "text-align:center;'>"

                + "© FNF Reminder System"

                + "</div>"

                + "</body>"
                + "</html>";
    }

    public void sendOtpEmail(
            String email,
            String otp) {

        log.info(
                "<<START>> sendOtpEmail <<START>>"
        );

        try {

            MimeMessage mimeMessage =
                    javaMailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            mimeMessage,
                            true,
                            "UTF-8"
                    );

            helper.setTo(email);

            helper.setSubject(
                    "FNF - OTP Verification"
            );

            String html =
                    buildOtpTemplate(
                            otp
                    );

            helper.setText(
                    html,
                    true
            );

            javaMailSender.send(
                    mimeMessage
            );

            log.info(
                    "OTP email sent successfully. email={}",
                    email
            );

        } catch (Exception ex) {

            log.error(
                    "Failed to send OTP email. email={}",
                    email,
                    ex
            );

            throw new RuntimeException(
                    "Failed to send OTP email"
            );
        }

        log.info(
                "<<END>> sendOtpEmail <<END>>"
        );
    }
    private String buildOtpTemplate(
            String otp) {

        return "<!DOCTYPE html>"
                + "<html>"
                + "<body style='font-family:Arial,sans-serif;background:#f4f6f9;padding:20px;'>"

                + "<div style='max-width:600px;margin:auto;background:#ffffff;border-radius:10px;overflow:hidden;'>"

                + "<div style='background:#2563eb;padding:25px;text-align:center;'>"
                + "<h1 style='color:white;margin:0;'>FNF</h1>"
                + "<p style='color:white;margin-top:8px;'>Family & Friends</p>"
                + "</div>"

                + "<div style='padding:30px;'>"

                + "<h2 style='color:#333;'>Verify Your Account</h2>"

                + "<p style='font-size:15px;color:#555;'>"
                + "Use the OTP below to complete your verification process."
                + "</p>"

                + "<div style='margin:30px 0;text-align:center;'>"
                + "<span style='font-size:32px;font-weight:bold;"
                + "letter-spacing:8px;color:#2563eb;'>"
                + otp
                + "</span>"
                + "</div>"

                + "<p style='color:#555;font-size:14px;'>"
                + "This OTP is valid for <b>5 minutes</b>."
                + "</p>"

                + "<p style='color:#555;font-size:14px;'>"
                + "If you did not request this OTP, please ignore this email."
                + "</p>"

                + "</div>"

                + "<div style='background:#f8fafc;padding:15px;text-align:center;color:#888;font-size:12px;'>"
                + "© FNF Application. All Rights Reserved."
                + "</div>"

                + "</div>"

                + "</body>"
                + "</html>";
    }
}