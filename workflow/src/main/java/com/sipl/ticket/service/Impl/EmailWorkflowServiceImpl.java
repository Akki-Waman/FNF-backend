package com.sipl.ticket.service.Impl;

import com.sipl.notification.callback.NotificationCallback;
import com.sipl.notification.dto.request.EmailNotificationRequest;
import com.sipl.notification.dto.response.NotificationResponse;
import com.sipl.notification.enums.NotificationPriority;
import com.sipl.ticket.core.dao.entity.Ticket;
import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dao.entity.WorkflowInstance;
import com.sipl.ticket.core.dao.entity.WorkflowSteps;
import com.sipl.ticket.core.dao.repository.*;
import com.sipl.ticket.core.dto.request.MailSendRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.enums.WorkFlowStatusEnum;
import com.sipl.ticket.core.mapper.WorkflowInstanceMapper;
import com.sipl.ticket.core.util.EmailUtil;
import com.sipl.ticket.core.util.UserManager;
import com.sipl.ticket.service.EmailWorkflowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import com.sipl.notification.service.impl.Notification;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailWorkflowServiceImpl implements EmailWorkflowService {

    private final WorkflowStepsRepository workflowStepsRepository;
    private final UserRolesRepository userRolesRepository;
    private final TicketRepository ticketRepository;
    private final EmailUtil emailService;
    private final WorkflowInstanceMapper workflowInstanceMapper;
    private final Notification notification;
    private final UsersRepository usersRepository;
    private final WorkflowInstanceRepository workflowInstanceRepository;
    private final @Qualifier("helperUserManager") UserManager userManager;
    private final HttpServletRequest request;


    @Value("${ticket_approval_request_created}")
    private Long ticketApprovalRequestTemplate;

    @Value("${ticket_approval_request_rejected}")
    private Long ticketApprovalRequestRejectedTemplate;

    @Value("${ticket_approval_request_approved}")
    private Long ticketApprovalRequestApprovedTemplate;

    @Value("${ticket_approval_request_next_stage}")
    private Long ticketApprovalRequestNextStageTemplate;

    @Value("${sender_mail}")
    private String senderMail;

    public String sendWorkflowEmail(
            WorkflowInstance workflowInstances,
            Users user,
            WorkflowSteps workflowSteps,
            String stepStatus) {
        Set<String> toSet = null;
        WorkflowInstance workflowInstance=workflowInstanceRepository.findById(workflowInstances.getWorkflowInstanceId()).orElse(null);
        WorkflowSteps step = workflowStepsRepository.findWorkflowStepsId(workflowInstance.getCurrentStep().getWorkFlowStepsId());
        if (workflowInstance.getWorkFlowStatus() == WorkFlowStatusEnum.IN_PROGRESS.getCode()) {
            toSet = prepareToNextStageInform(workflowInstance,step, user);
        } else {
            toSet = prepareToList(workflowInstance,step, user);
        }
        List<String> ccList = prepareCcList(workflowInstance,step, user);

        MailSendRequestDTO mailDto = buildMailDTO(workflowInstance, workflowSteps, user, stepStatus,
                new ArrayList<>(toSet), ccList);
        log.info("Template ID received: {}", mailDto.getTemplateId());
        EmailNotificationRequest emailRequest = buildEmailRequest(mailDto);
        try {
            log.info("Sending email with templateId: {}", mailDto.getTemplateId());
            log.info("To: {}", mailDto.getNotifiedEmailId());
            log.info("CC: {}", mailDto.getEmailIds());
            sendEmail(emailRequest, "Approval" + mailDto.getTicketId());
        } catch (Exception e) {
            log.error("Exception while sending email: ", e);
            return "Failed";
        }
        return "Sent";
    }

    private void sendEmail(EmailNotificationRequest emailRequest, String requestId) throws Exception {
        log.info("Sending email to: {}", emailRequest.getTo());

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
    }


    private EmailNotificationRequest buildEmailRequest(MailSendRequestDTO mailDto) {
        EmailNotificationRequest emailRequest = new EmailNotificationRequest();
        emailRequest.setTo(mailDto.getNotifiedEmailId());
        emailRequest.setCc(mailDto.getEmailIds());
        emailRequest.setSender(senderMail);
        emailRequest.setPriority(NotificationPriority.DEFAULT);
        String status = mailDto.getStatus();
        emailRequest.setTemplateId(mailDto.getTemplateId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = mailDto.getDate().format(formatter);
        Map<String, String> templateData = new HashMap<>();
        templateData.put("TICKET_ID", mailDto.getTicketId());
        templateData.put("STATUS", status);
        templateData.put("DATE", formattedDate);
        templateData.put("TICKET_SUBJECT", mailDto.getTicketSubject());
        templateData.put("PERFORMED_BY", mailDto.getPerformedBy());
        templateData.put("APPROVAL_REQUEST_REASON",
                Optional.ofNullable(mailDto.getRaisedReason()).orElse("N/A"));
        templateData.put("SUBJECT", mailDto.getSubject());
        emailRequest.setTemplateData(templateData);
        log.info("Sending email to: {}", mailDto.getNotifiedEmailId());
        return emailRequest;
    }

    private Set<String> prepareToNextStageInform(WorkflowInstance workflowInstance,WorkflowSteps step, Users user) {
        Set<String> toSet = new LinkedHashSet<>();
      //  WorkflowSteps step = workflowInstance.getCurrentStep();
        log.info("Assignment mode: {}", step.getAssignmentMode());
        log.info("Role ID: {}", step.getRole() != null ? step.getRole().getUserRoleId() : null);
        log.info("Assigned user email: {}", workflowInstance.getAssignedUser() != null ? workflowInstance.getAssignedUser().getEmailId() : null);
        if (step == null) {
            log.warn("Workflow step is null for instance: {}", workflowInstance.getWorkflowInstanceId());
            return toSet;
        }
        if (step.getAssignmentMode() != null) {
            if (step.getAssignmentMode().equals(1)) { // Role-based
                if (step.getRole() != null) {
                    List<String> roleUsers = userRolesRepository.findActiveUsersByRoleId(step.getRole().getUserRoleId())
                            .stream()
                            .map(Users::getEmailId)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    toSet.addAll(roleUsers);
                }
            } else if (step.getAssignmentMode().equals(2)) { // User-based
                if (workflowInstance.getAssignedUser() != null && workflowInstance.getAssignedUser().getEmailId() != null) {
                    toSet.add(workflowInstance.getAssignedUser().getEmailId());
                }
            }
        }
        return toSet;
    }

    private List<String> prepareCcList(WorkflowInstance workflowInstance,WorkflowSteps step, Users user) {
        List<String> ccList = new ArrayList<>();
        if (workflowInstance.getCreatedBy() != null) {
            Users userData=usersRepository.findById(workflowInstance.getCreatedBy().getId()).orElse(null);
            ccList.add(userData.getEmailId());
        }
        return ccList;
    }

    private Set<String> prepareToList(WorkflowInstance workflowInstance,WorkflowSteps step, Users user) {
        Set<String> toSet = new LinkedHashSet<>();
      //  WorkflowSteps step = workflowStepsRepository.findWorkflowStepsId(workflowInstance.getCurrentStep().getWorkFlowStepsId());
        log.info("Assignment mode: {}", step.getAssignmentMode());
        log.info("Role ID: {}", step.getRole() != null ? step.getRole().getUserRoleId() : null);
        log.info("Assigned user email: {}", workflowInstance.getAssignedUser() != null ? workflowInstance.getAssignedUser().getEmailId() : null);
        if (step != null && step.getAssignmentMode() != null) {
            if (step.getAssignmentMode().equals(1)) { // Role-based
                if (step.getRole() != null) {
                    List<String> roleUsers = userRolesRepository.findActiveUsersByRoleId(step.getRole().getUserRoleId())
                            .stream()
                            .map(Users::getEmailId)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    toSet.addAll(roleUsers);
                }
            } else if (step.getAssignmentMode().equals(2)) { // User-based
                if (workflowInstance.getAssignedUser() != null &&
                        workflowInstance.getAssignedUser().getEmailId() != null) {
                    toSet.add(workflowInstance.getAssignedUser().getEmailId());
                }
            }
        }
        if (user.getEmailId() != null) toSet.remove(user.getEmailId());
        return toSet;
    }

    private MailSendRequestDTO buildMailDTO(
            WorkflowInstance workflowInstance,
            WorkflowSteps workflowSteps,
            Users user,
            String stepStatus,
            List<String> toList,
            List<String> ccList) {

        Ticket ticket = ticketRepository.findById(workflowInstance.getEntityId())
                .orElseThrow(() -> new RuntimeException("Ticket ID not found: " + workflowInstance.getEntityId()));
        Users userData = userManager.getUser(request);
        String userName = userData != null
                ? Optional.ofNullable(userData.getFirstName()).orElse("") + " " + Optional.ofNullable(userData.getLastName()).orElse("")
                : "Unknown User";

        String roleName = workflowSteps.getRole() != null ? workflowSteps.getRole().getUserRoleName() : "Unknown Role";

        MailSendRequestDTO dto = new MailSendRequestDTO();
        dto.setNotifiedEmailId(toList);
        dto.setEmailIds(ccList);
        dto.setStatus(stepStatus);
        String subject = "TICKET-" + workflowInstance.getEntityId() + " Approval Request – " + stepStatus;
        dto.setSubject(subject);
        dto.setDate(LocalDate.now());
        dto.setTicketId(ticket.getTicketId().toString());
        dto.setPerformedBy(userName + " (" + roleName + ")");
        dto.setRaisedReason(workflowInstance.getReason());
        dto.setTicketSubject(
                Optional.ofNullable(ticket.getSubject()).orElse("N/A")
        );
        switch (stepStatus) {
            case "REJECTED": dto.setTemplateId(ticketApprovalRequestRejectedTemplate); break;
            case "APPROVED":dto.setTemplateId(ticketApprovalRequestApprovedTemplate);break;
            case "IN_PROGRESS": dto.setTemplateId(ticketApprovalRequestNextStageTemplate); break;
            case "CREATED": dto.setTemplateId(ticketApprovalRequestTemplate); break;
        }
        return dto;
    }

}
