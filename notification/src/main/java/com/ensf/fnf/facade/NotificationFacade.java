package com.ensf.fnf.facade;

import com.ensf.fnf.core.dao.entity.UserEntity;

public interface NotificationFacade {

    void sendAccountCreatedNotification(
            UserEntity user
    );

    void sendOtpNotification(
            UserEntity user,
            String otp
    );
}