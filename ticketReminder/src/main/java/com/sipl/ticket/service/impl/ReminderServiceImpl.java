package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.MasterContext;
import com.sipl.ticket.core.dao.entity.Masters;
import com.sipl.ticket.core.dao.entity.ReminderRecipient;
import com.sipl.ticket.core.dao.entity.TicketReminder;
import com.sipl.ticket.core.dao.repository.MastersRepository;
import com.sipl.ticket.core.dao.repository.TicketReminderRepository;
import com.sipl.ticket.core.dto.request.ReminderCreateRequestDto;
import com.sipl.ticket.core.dto.request.ReminderRecipientRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ReminderResponseDto;
import com.sipl.ticket.core.dto.response.ReminderRecipientResponseDto;
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

    @Override
    public ApiResponseDTO<ReminderResponseDto> createReminder(
            ReminderCreateRequestDto request) {

        log.info("Creating reminder for ticketId={}", request.getTicketId());

        try {

            TicketReminder reminder = new TicketReminder();
            reminder.setTicketId(request.getTicketId());
            reminder.setReminderTime(request.getReminderTime());
            reminder.setNextRunTime(request.getReminderTime());
            reminder.setIsRecurring(request.getIsRecurring());
            reminder.setRecurrenceInterval(request.getRecurrenceInterval());
            reminder.setRecurrenceEndTime(request.getRecurrenceEndTime());
            reminder.setStatus(1);
            reminder.setRetryCount(0);
            reminder.setMaxRetry(3);

            log.info("Reminder time={}, nextRunTime={}",
                    reminder.getReminderTime(), reminder.getNextRunTime());

            List<ReminderRecipient> recipients = new ArrayList<>();

            if (request.getRecipients() != null) {

                log.info("Total recipients received={}", request.getRecipients().size());

                for (ReminderRecipientRequestDto r : request.getRecipients()) {

                    ReminderRecipient rec = new ReminderRecipient();
                    rec.setUserId(r.getUserId());
                    rec.setChannelType(mapChannel(r.getChannelType()));
                    rec.setStatus(1);
                    rec.setRetryCount(0);
                    rec.setReminder(reminder);

                    log.info("Recipient userId={}, channelType={}",
                            rec.getUserId(), rec.getChannelType());

                    recipients.add(rec);
                }
            }

            reminder.setRecipients(recipients);

            TicketReminder saved = reminderRepository.save(reminder);

            log.info("Reminder saved with id={}", saved.getId());

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

            log.info("Status map={}", statusMap);
            log.info("Channel map={}", channelMap);

            MasterContext context = new MasterContext(null, statusMap, channelMap);

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

    private Integer mapChannel(String channel) {

        if (channel == null) return 0;

        if ("EMAIL".equalsIgnoreCase(channel)) return 1;
        if ("WHATSAPP".equalsIgnoreCase(channel)) return 2;
        if ("SMS".equalsIgnoreCase(channel)) return 3;


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
                        reminder.getId(), reminder.getNextRunTime());

                try {

                    if (reminder.getRecipients() == null || reminder.getRecipients().isEmpty()) {
                        log.warn("No recipients found for reminder id={}", reminder.getId());
                        continue;
                    }

                    log.info("Recipients count for reminder {} = {}",
                            reminder.getId(), reminder.getRecipients().size());

                    for (ReminderRecipient recipient : reminder.getRecipients()) {

                        log.info("Sending notification to userId={}, channelType={}",
                                recipient.getUserId(), recipient.getChannelType());

                        sendNotification(recipient);

                        recipient.setStatus(3);
                        recipient.setSentAt(LocalDateTime.now());

                        log.info("Notification sent. Updated recipient userId={}, sentAt={}",
                                recipient.getUserId(), recipient.getSentAt());
                    }

                    if (Boolean.TRUE.equals(reminder.getIsRecurring())) {

                        log.info("Reminder id={} is recurring", reminder.getId());
                        updateNextRun(reminder);

                    } else {

                        reminder.setStatus(3);
                        log.info("Reminder id={} marked as completed", reminder.getId());
                    }

                } catch (Exception e) {

                    reminder.setRetryCount(reminder.getRetryCount() + 1);
                    reminder.setLastError(e.getMessage());

                    log.error("Error processing reminder id={}", reminder.getId(), e);
                }
            }

            reminderRepository.saveAll(reminders);

            log.info("All reminders processed and saved");

        } catch (Exception e) {
            log.error("Error processing reminders", e);
        }
    }

    private void sendNotification(ReminderRecipient recipient) {

        log.info("Sending notification to userId={} channelType={}",
                recipient.getUserId(),
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