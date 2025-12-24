package com.sipl.collaboration.notificationService.service;

import com.sipl.collaboration.core.dto.request.MailSendRequestDTO;
import com.sipl.collaboration.core.dto.response.ApiResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    ApiResponseDTO<Void> sendMail(MailSendRequestDTO mailSendRequestDto);
}