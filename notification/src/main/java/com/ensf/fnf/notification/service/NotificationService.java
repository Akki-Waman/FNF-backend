package com.ensf.fnf.notification.service;

import com.ensf.fnf.core.dao.entity.UserEntity;

public interface NotificationService {
    void sendWelcomeEmail(
            String email,
            String fullName,
            String mobileNumber
    );
    void sendAccountCreatedNotification(
            UserEntity user
    );

    void sendWelcomeSms(
            String mobileNumber,
            String fullName
    );

    void sendOtpEmail(
            String email,
            String otp
    );

    void sendOtpSms(
            String mobileNumber,
            String otp
    );
}