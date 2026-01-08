package com.sipl.ticket.core.util;

import com.sipl.notification.callback.NotificationCallback;
import com.sipl.notification.dto.request.EmailNotificationRequest;
import com.sipl.notification.dto.response.NotificationResponse;
import com.sipl.notification.service.impl.Notification;
import com.sipl.ticket.core.dao.entity.EmailNotificationLogs;
import com.sipl.ticket.core.dao.repository.EmailNotificationLogsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailUtil {

    private final Notification notification;
    private final EmailNotificationLogsRepository emailNotificationLogsRepository;

    public void sendEmail(
            EmailNotificationRequest emailRequest,
            String requestId
    ) throws Exception {

        notification.sendEmail(
                emailRequest,
                requestId,
                null,
                new NotificationCallback() {

                    @Override
                    public void onSuccess(
                            ResponseEntity<NotificationResponse<?>> responseEntity) {

                        log.info(
                                "Email sent successfully. Response: {}",
                                Objects.toString(
                                        responseEntity.getBody(),
                                        "No response body"));
                    }

                    @Override
                    public void onFailure(
                            ResponseEntity<NotificationResponse<?>> responseEntity) {

                        log.error(
                                "Email sending failed. Response: {}",
                                Objects.toString(
                                        responseEntity.getBody(),
                                        "No response body"));
                    }
                }
        );
    }

    public  void saveEmailLog(
            List<String> recipients,
            String subject,
            String body,
            String sender,
            String status,
            String responseMessage
    ) {
        try {
            EmailNotificationLogs log = new EmailNotificationLogs();

            log.setRecipientEmails(String.join(",", recipients));
            log.setSubject(subject);
            log.setBody(body);
            log.setSenderEmail(sender);
            log.setStatus(status); // SUCCESS / FAILED
            log.setResponseMessage(responseMessage);
            log.setSentAt(LocalDateTime.now());

            emailNotificationLogsRepository.save(log);

        } catch (Exception e) {
            log.error("Failed to save email notification log", e);
        }
    }

}
