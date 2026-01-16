package com.sipl.ticket.report.service.impl;

import com.sipl.ticket.core.dao.entity.ClientProducts;
import com.sipl.ticket.core.dao.entity.Masters;
import com.sipl.ticket.core.dao.entity.ServiceEntity;
import com.sipl.ticket.core.dao.entity.Ticket;
import com.sipl.ticket.core.dao.repository.MastersRepository;
import com.sipl.ticket.core.dao.repository.TicketRepository;
import com.sipl.ticket.core.dto.request.ResponsePenaltyRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ResponsePenaltyResponseDTO;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final TicketRepository ticketRepository;
    private final MastersRepository mastersRepository;
    private static final Integer TICKET_STATUS_COLUMN_CODE = 2;

    @Override
    public ApiResponseDTO<PagedResponse<ResponsePenaltyResponseDTO>>
    searchResponsePenaltyReport(ResponsePenaltyRequestDTO dto) {

        try {
            Pageable pageable = PaginationUtil.pageable(
                    dto.getPage(),
                    dto.getSize(),
                    dto.getSortBy(),
                    dto.getSortDir()
            );

            Page<Ticket> pageResult =
                    ticketRepository.searchResponsePenaltyReport(
                            dto.getTicketId(),
                            dto.getUnitName(),
                            dto.getDeviceName(),
                            dto.getService(),
                            pageable
                    );

            if (pageResult.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No response penalty records found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<ResponsePenaltyResponseDTO> content =
                    pageResult.getContent()
                            .stream()
                            .map(this::mapToResponsePenaltyDto)
                            .collect(Collectors.toList());

            PagedResponse<ResponsePenaltyResponseDTO> pagedResponse =
                    new PagedResponse<>(
                            content,
                            pageResult.getNumber(),
                            pageResult.getTotalElements(),
                            pageResult.getTotalPages(),
                            pageResult.getSize(),
                            pageResult.isLast()
                    );

            return new ApiResponseDTO<>(
                    pagedResponse,
                    "Response penalty report fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("searchResponsePenaltyReport error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    private ResponsePenaltyResponseDTO mapToResponsePenaltyDto(Ticket ticket) {

        ResponsePenaltyResponseDTO dto = new ResponsePenaltyResponseDTO();

        dto.setTicketId(ticket.getTicketId());
        dto.setSubject(ticket.getSubject());

        dto.setUnitName(
                Optional.ofNullable(ticket.getClientProducts())
                        .map(ClientProducts::getGroupName)
                        .orElse(null)
        );

        dto.setDeviceName(
                Optional.ofNullable(ticket.getClientProducts())
                        .map(ClientProducts::getDeviceName)
                        .orElse(null)
        );

        dto.setService(
                Optional.ofNullable(ticket.getService())
                        .map(ServiceEntity::getServiceName)
                        .orElse(null)
        );

        dto.setIssueLogged(
                Optional.ofNullable(ticket.getCreatedTime())
                        .map(LocalDateTime::toLocalDate)
                        .orElse(null)
        );

        dto.setIssueResolved(
                Optional.ofNullable(ticket.getResolutionDateTime())
                        .map(LocalDateTime::toLocalDate)
                        .orElse(null)
        );

        dto.setResolutionTime(convertHoursToDays(ticket.getResolutionTimeHours()));

        dto.setPenaltyPercentage(
                Optional.ofNullable(ticket.getResponsePenaltyPercentage())
                        .map(BigDecimal::doubleValue)
                        .orElse(null)
        );

        dto.setStatus(setTicketStatus(ticket));
        dto.setTaskStatus(null); //TODO: don't what exact value is need to set

        dto.setWithInWeek(
                ticket.getResolutionDateTime() != null &&
                        ticket.getCreatedTime() != null &&
                        Duration.between(
                                ticket.getCreatedTime(),
                                ticket.getResolutionDateTime()
                        ).toDays() <= 7
        );

        dto.setPenaltyDays(
                ticket.getResponsePenaltyTime() != null
                        ? convertHoursToDays(ticket.getResponsePenaltyTime())
                        : null
        );

        return dto;
    }

    public static String convertHoursToDays(Double totalHours) {

        if (totalHours == null || totalHours <= 0) {
            return "0 Days";
        }

        int days = (int) (totalHours / 24);
        double remainingHours = totalHours % 24;

        remainingHours = Math.round(remainingHours * 100.0) / 100.0;

        if (days > 0 && remainingHours > 0) {
            return days + " Days " + remainingHours + " Hours";
        } else if (days > 0) {
            return days + " Days";
        } else {
            return remainingHours + " Hours";
        }
    }

    private String setTicketStatus(Ticket ticket) {

        if (ticket.getStatus() == null) {
            return null;
        }

        Masters masters = mastersRepository.findByColumnCodeAndColumnValue(
                TICKET_STATUS_COLUMN_CODE,
                ticket.getStatus()
        );

        return masters != null
                ? masters.getValueDesc()
                : String.valueOf(ticket.getStatus());
    }

}
