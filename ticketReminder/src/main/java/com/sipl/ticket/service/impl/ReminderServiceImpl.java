package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.*;
import com.sipl.ticket.core.dao.repository.MastersRepository;
import com.sipl.ticket.core.dao.repository.TicketReminderRepository;
import com.sipl.ticket.core.dao.repository.TicketRepository;
import com.sipl.ticket.core.dao.repository.UsersRepository;
import com.sipl.ticket.core.dto.request.ReminderCreateRequestDto;
import com.sipl.ticket.core.dto.request.ReminderRecipientRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ReminderResponseDto;
import com.sipl.ticket.core.mapper.TicketReminderMapper;
import com.sipl.ticket.service.ReminderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final TicketReminderRepository reminderRepository;
    private final TicketReminderMapper reminderMapper;
    private final MastersRepository mastersRepository;
    private final TicketRepository ticketRepository;
    private final UsersRepository usersRepository;

    @Override
    public ApiResponseDTO<ReminderResponseDto> createReminder(
            ReminderCreateRequestDto request) {

        log.info("Creating reminder for ticketId={}", request.getTicketId());

        try {

            TicketReminder reminder = buildReminder(request);

            List<ReminderRecipient> recipients = buildRecipients(request, reminder);
            reminder.setRecipients(recipients);

            TicketReminder saved = reminderRepository.save(reminder);

            log.info("Reminder saved with id={}", saved.getTicketReminderId());

            MasterContext context = buildMasterContext();

            ReminderResponseDto responseDto =
                    reminderMapper.toDto(saved, context);

            return new ApiResponseDTO<>(
                    responseDto,
                    "Reminder created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {

            log.error("Error while creating reminder", e);

            return new ApiResponseDTO<>(
                    null,
                    "Failed to create reminder",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    private TicketReminder buildReminder(ReminderCreateRequestDto request) {

        TicketReminder reminder = new TicketReminder();
        log.info("Checking ticketId: {}", request.getTicketId());
        log.info("Ticket exists? {}", ticketRepository.existsById(request.getTicketId()));
        Ticket ticket = ticketRepository.findExactById(request.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        reminder.setTicket(ticket);
        reminder.setReminderTime(request.getReminderTime());
        reminder.setNextRunTime(request.getReminderTime());
        reminder.setIsRecurring(request.getIsRecurring());
        reminder.setRecurrenceInterval(request.getRecurrenceInterval());
        reminder.setRecurrenceEndTime(request.getRecurrenceEndTime());
        reminder.setStatus(1);
        reminder.setRetryCount(0);
        reminder.setMaxRetry(3);
        reminder.setIsActive(true);
        reminder.setIsDeleted(false);

        log.info("Reminder initialized for ticketId={}", request.getTicketId());
        log.debug("Reminder details: time={}, nextRunTime={}, recurring={}",
                reminder.getReminderTime(),
                reminder.getNextRunTime(),
                reminder.getIsRecurring());

        return reminder;
    }

    private List<ReminderRecipient> buildRecipients(
            ReminderCreateRequestDto request,
            TicketReminder reminder) {

        List<ReminderRecipient> recipients = new ArrayList<>();

        if (request.getRecipients() == null || request.getRecipients().isEmpty()) {
            log.warn("No recipients provided for ticketId={}", request.getTicketId());
            return recipients;
        }

        log.info("Processing {} recipients", request.getRecipients().size());

        for (ReminderRecipientRequestDto r : request.getRecipients()) {

            ReminderRecipient rec = new ReminderRecipient();

            Users user = usersRepository.findById(r.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            rec.setUser(user);
            rec.setChannelType(mapChannel(r.getChannelType()));

            rec.setReminder(reminder);
            rec.setIsActive(true);
            rec.setIsDeleted(false);

            log.info("Notification sent for userId={}, sentAt={}",
                    rec.getUser().getId(), rec.getChannelType());

            log.debug("Recipient raw input channelType={}", r.getChannelType());

            recipients.add(rec);
        }

        return recipients;
    }

    private MasterContext buildMasterContext() {

        Map<Integer, String> statusMap = mastersRepository
                .findByColumnNameIgnoreCase("reminder status")
                .stream()
                .collect(Collectors.toMap(
                        Masters::getColumnValue,
                        Masters::getValueDesc
                ));

        Map<Integer, String> channelMap = mastersRepository
                .findByColumnNameIgnoreCase("channel type")
                .stream()
                .collect(Collectors.toMap(
                        Masters::getColumnValue,
                        Masters::getValueDesc
                ));

        log.info("Master data loaded successfully");
        log.debug("Status map={}", statusMap);
        log.debug("Channel map={}", channelMap);

        return new MasterContext(null, statusMap, channelMap);
    }

    private Integer mapChannel(String channel) {

        if (channel == null) {
            log.warn("Channel type is null");
            return 0;
        }

        log.debug("Mapping channelType={}", channel);

        if ("EMAIL".equalsIgnoreCase(channel)) return 1;
        if ("WHATSAPP".equalsIgnoreCase(channel)) return 2;
        if ("SMS".equalsIgnoreCase(channel)) return 3;

        log.warn("Unknown channel type received={}", channel);

        return 0;
    }

    public void processReminders() {

        log.info("Processing reminders started at {}", LocalDateTime.now());

        try {

            List<TicketReminder> reminders =
                    reminderRepository.findDueReminders(LocalDateTime.now());

            log.info("Total reminders fetched={}", reminders.size());

            for (TicketReminder reminder : reminders) {

                log.info("Processing reminder id={}, nextRunTime={}",
                        reminder.getTicketReminderId(), reminder.getNextRunTime());

                try {

                    if (reminder.getRecipients() == null || reminder.getRecipients().isEmpty()) {
                        log.warn("No recipients found for reminder id={}", reminder.getTicketReminderId());
                        continue;
                    }

                    log.info("Recipients count for reminder {} = {}",
                            reminder.getTicketReminderId(), reminder.getRecipients().size());

                    for (ReminderRecipient recipient : reminder.getRecipients()) {

                        log.info("Notification sent for userId={},channelType={} ",
                                recipient.getUser().getId(), recipient.getChannelType());

                        sendNotification(recipient);


                        log.info("Notification sent for userId={}",
                                recipient.getUser().getId());
                    }

                    if (Boolean.TRUE.equals(reminder.getIsRecurring())) {

                        log.info("Reminder id={} is recurring", reminder.getTicketReminderId());
                        updateNextRun(reminder);

                    } else {

                        reminder.setStatus(3);
                        log.info("Reminder id={} marked as completed", reminder.getTicketReminderId());
                    }

                } catch (Exception e) {

                    reminder.setRetryCount(reminder.getRetryCount() + 1);
                    reminder.setLastError(e.getMessage());

                    log.error("Error processing reminder id={}", reminder.getTicketReminderId(), e);
                }
            }

            reminderRepository.saveAll(reminders);

            log.info("All reminders processed and saved");

        } catch (Exception e) {
            log.error("Error processing reminders", e);
        }
    }

    private void sendNotification(ReminderRecipient recipient) {

        log.info("Sending notification to userId={} ,channelType={}",
                recipient.getUser().getId(),
                recipient.getChannelType());
    }

    private void updateNextRun(TicketReminder reminder) {

        LocalDateTime next = reminder.getNextRunTime();

        Integer type = reminder.getRecurrenceInterval();

        if (type != null) {
            if (type == 1) {
                next = next.plusDays(1);
            } else if (type == 2) {
                next = next.plusWeeks(1);
            } else if (type == 3) {
                next = next.plusMonths(1);
            }
        }

        if (reminder.getRecurrenceEndTime() != null &&
                next.isAfter(reminder.getRecurrenceEndTime())) {

            reminder.setStatus(3);

        } else {
            reminder.setNextRunTime(next);
        }
    }
}