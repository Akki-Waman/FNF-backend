package com.sipl.ticket.notificationService.service.impl;

import com.sipl.ticket.core.dao.repository.UserRolesRepository;
import com.sipl.ticket.core.dao.repository.UsersRepository;
import com.sipl.ticket.core.dto.request.MailSendRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.notificationService.service.EmailService;
import com.sipl.notification.callback.NotificationCallback;
import com.sipl.notification.dto.request.EmailNotificationRequest;
import com.sipl.notification.dto.response.NotificationResponse;
import com.sipl.notification.enums.NotificationPriority;
import com.sipl.notification.service.impl.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${email.request.id}")
    private String requestId;

    @Value("${sender.mail}")
    private String senderMail;

    private final Notification notification;
    private final UserRolesRepository userRolesRepository;
    private final UsersRepository usersRepository;

    @Override
    public ApiResponseDTO<Void> sendMail(MailSendRequestDTO mailSendRequestDto) {
        try {
            EmailNotificationRequest emailRequest = new EmailNotificationRequest();
            emailRequest.setTo(mailSendRequestDto.getNotifiedEmailId());
            emailRequest.setCc(mailSendRequestDto.getEmailIds());
            emailRequest.setSender(senderMail);
            emailRequest.setPriority(NotificationPriority.DEFAULT);

            String status = mailSendRequestDto.getStatus();

            emailRequest.setTemplateId(mailSendRequestDto.getTemplateId());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = mailSendRequestDto.getDate().format(formatter);

            Map<String, String> templateData = new HashMap<>();
            templateData.put("TRANSACTION_ID", mailSendRequestDto.getTransactionId());
            templateData.put("STATUS", status);
            templateData.put("DATE", formattedDate);
            templateData.put("PERFORMED_BY", mailSendRequestDto.getPerformedBy());
            templateData.put("CANCELLATION_REASON",
                    Optional.ofNullable(mailSendRequestDto.getCancellationReason()).orElse("N/A"));
            templateData.put("SR_NO", "1");
            templateData.put("LEP_ISSUE_DATE", mailSendRequestDto.getLepIssueDate().toString());
            templateData.put("LEP_NUMBER", mailSendRequestDto.getLepNumber());
            templateData.put("CANCELLATION_REQUEST_BY", mailSendRequestDto.getCancellationRequestBy());
            templateData.put("CANCELLATION_INITIATED_TIME",mailSendRequestDto.getCancellationInititatedTime().toString());
            templateData.put("TRUCK_NUMBER", mailSendRequestDto.getTruckNumber());
            templateData.put("DRIVER_NAME", mailSendRequestDto.getDriverName());
            templateData.put("RFID_NUMBER", mailSendRequestDto.getRfidNumber());

            emailRequest.setTemplateData(templateData);

            log.info("Sending email to: {}", mailSendRequestDto.getNotifiedEmailId());
            notification.sendEmail(emailRequest, requestId, null, new NotificationCallback() {
                @Override
                public void onSuccess(ResponseEntity<NotificationResponse<?>> responseEntity) {
                    log.info("Email sent successfully: {}", responseEntity.getBody());
                }
                @Override
                public void onFailure(ResponseEntity<NotificationResponse<?>> responseEntity) {
                    log.error("Failed to send email: {}", responseEntity.getBody());
                }
            });

            return new ApiResponseDTO<>(null, "success", HttpStatus.OK, false);

        } catch (Exception e) {
            log.error("Exception while sending email", e);
            return new ApiResponseDTO<>(null, "Error while sending email", HttpStatus.INTERNAL_SERVER_ERROR, true);
        }
    }

}
