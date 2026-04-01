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

import javax.annotation.PostConstruct;
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

    private Map<Integer, String> recurrenceCache;

    @PostConstruct
    public void init() {
        recurrenceCache = mastersRepository
                .findByColumnNameIgnoreCase("recurrence interval")
                .stream()
                .collect(Collectors.toMap(
                        Masters::getColumnValue,
                        Masters::getValueDesc
                ));
        log.debug("Recurrence cache loaded={}", recurrenceCache);
    }

    @Override
    public ApiResponseDTO<ReminderResponseDto> createReminder(ReminderCreateRequestDto request) {

        log.info("Create reminder request received ticketId={}, reminderTime={}",
                request.getTicketId(), request.getReminderTime());

        log.debug("Incoming request payload={}", request);

        try {

            normalizeRequest(request);

            validateDuplicate(request);

            validateDateOverlap(request);

            TicketReminder reminder = buildReminder(request);

            log.debug("Reminder entity after build={}", reminder);

            List<ReminderRecipient> recipients = buildRecipients(request, reminder);

            log.debug("Recipients prepared count={}", recipients.size());

            reminder.setRecipients(recipients);

            TicketReminder saved = reminderRepository.save(reminder);

            log.info("Reminder successfully created id={}", saved.getTicketReminderId());

            MasterContext context = buildMasterContext();

            ReminderResponseDto response = reminderMapper.toDto(saved, context);

            log.debug("Response DTO generated={}", response);

            return new ApiResponseDTO<>(
                    response,
                    "Reminder created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {

            log.error("Error while creating reminder ticketId={}", request.getTicketId(), e);

            return new ApiResponseDTO<>(
                    null,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    private void normalizeRequest(ReminderCreateRequestDto request) {

        if (Boolean.FALSE.equals(request.getIsRecurring())) {
            request.setRecurrenceInterval(null);
            request.setRecurrenceEndTime(null);
            log.debug("Recurring false → interval & endTime cleared");
        }
    }

    private void validateDuplicate(ReminderCreateRequestDto request) {

        Long count = reminderRepository.countReminder(
                request.getTicketId(),
                request.getReminderTime()
        );

        log.debug("Duplicate count={}", count);

        if (count != null && count > 0) {
            throw new RuntimeException("Reminder already exists for same ticket and time");
        }
    }

    private void validateDateOverlap(ReminderCreateRequestDto request) {

        List<TicketReminder> existing = reminderRepository
                .findByTicket_TicketIdAndIsDeletedFalse(request.getTicketId());

        LocalDateTime newStart = request.getReminderTime();
        LocalDateTime newEnd = request.getRecurrenceEndTime();

        boolean isNewRecurring = Boolean.TRUE.equals(request.getIsRecurring());

        for (TicketReminder r : existing) {

            LocalDateTime existingStart = r.getReminderTime();
            LocalDateTime existingEnd = r.getRecurrenceEndTime();

            boolean isExistingRecurring = Boolean.TRUE.equals(r.getIsRecurring());

            log.debug("Checking overlap newStart={}, newEnd={}, existingStart={}, existingEnd={}",
                    newStart, newEnd, existingStart, existingEnd);

            if (isNewRecurring && isExistingRecurring) {

                if (existingEnd == null || newEnd == null) continue;

                boolean overlap =
                        (newStart.isBefore(existingEnd) || newStart.isEqual(existingEnd)) &&
                                (newEnd.isAfter(existingStart) || newEnd.isEqual(existingStart));

                if (overlap) {
                    throw new RuntimeException("Reminder already exists in given date range");
                }
            } else if (!isNewRecurring && isExistingRecurring) {

                if (existingEnd == null) continue;

                boolean overlap =
                        (newStart.isAfter(existingStart) || newStart.isEqual(existingStart)) &&
                                (newStart.isBefore(existingEnd) || newStart.isEqual(existingEnd));

                if (overlap) {
                    throw new RuntimeException("Reminder already exists in given date range");
                }
            } else if (isNewRecurring && !isExistingRecurring) {

                if (newEnd == null) continue;

                boolean overlap =
                        (existingStart.isAfter(newStart) || existingStart.isEqual(newStart)) &&
                                (existingStart.isBefore(newEnd) || existingStart.isEqual(newEnd));

                if (overlap) {
                    throw new RuntimeException("Reminder already exists in given date range");
                }
            } else {

                if (newStart.isEqual(existingStart)) {
                    throw new RuntimeException("Reminder already exists for same time");
                }
            }
        }
    }

    private TicketReminder buildReminder(ReminderCreateRequestDto request) {

        Ticket ticket = ticketRepository.findExactById(request.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        TicketReminder reminder = new TicketReminder();

        reminder.setTicket(ticket);
        reminder.setReminderTime(request.getReminderTime());
        reminder.setNextRunTime(request.getReminderTime());
        reminder.setIsRecurring(request.getIsRecurring());

        Integer interval = resolveRecurrenceInterval(request);
        reminder.setRecurrenceInterval(interval);
        reminder.setRecurrenceEndTime(request.getRecurrenceEndTime());
        reminder.setStatus(1);
        reminder.setRetryCount(0);
        reminder.setMaxRetry(3);
        reminder.setIsActive(true);
        reminder.setIsDeleted(false);

        return reminder;
    }

    private Integer resolveRecurrenceInterval(ReminderCreateRequestDto request) {

        if (!Boolean.TRUE.equals(request.getIsRecurring())) return null;

        Integer interval = request.getRecurrenceInterval();

        if (interval == null) {
            if (!recurrenceCache.containsKey(1)) {
                throw new RuntimeException("Default recurrence interval not configured");
            }
            return 1;
        }

        if (!recurrenceCache.containsKey(interval)) {
            throw new RuntimeException("Invalid recurrence interval");
        }

        return interval;
    }

    private List<ReminderRecipient> buildRecipients(
            ReminderCreateRequestDto request,
            TicketReminder reminder) {

        List<ReminderRecipient> recipients = new ArrayList<>();

        if (request.getRecipients() == null || request.getRecipients().isEmpty()) {
            return recipients;
        }

        for (ReminderRecipientRequestDto r : request.getRecipients()) {

            Users user = usersRepository.findById(r.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ReminderRecipient rec = new ReminderRecipient();

            rec.setUser(user);
            rec.setChannelType(mapChannel(r.getChannelType()));
            rec.setReminder(reminder);
            rec.setIsActive(true);
            rec.setIsDeleted(false);

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

        return new MasterContext(null, statusMap, channelMap, recurrenceCache);
    }

    private Integer mapChannel(String channel) {

        if (channel == null) return 0;

        if ("EMAIL".equalsIgnoreCase(channel)) return 1;
        if ("WHATSAPP".equalsIgnoreCase(channel)) return 2;
        if ("SMS".equalsIgnoreCase(channel)) return 3;

        return 0;
    }
}