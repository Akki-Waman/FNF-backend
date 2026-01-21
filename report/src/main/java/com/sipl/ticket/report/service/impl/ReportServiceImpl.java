package com.sipl.ticket.report.service.impl;

import com.sipl.ticket.core.dao.entity.*;
import com.sipl.ticket.core.dao.repository.MastersRepository;
import com.sipl.ticket.core.dao.repository.TicketRepository;
import com.sipl.ticket.core.dao.repository.TicketResponseRepository;
import com.sipl.ticket.core.dto.request.ResponsePenaltyRequestDTO;
import com.sipl.ticket.core.dto.request.StaffTicketRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ResponsePenaltyResponseDTO;
import com.sipl.ticket.core.dto.response.StaffTicketResponseDTO;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.core.util.ResponsePenaltyExportHelper;
import com.sipl.ticket.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final TicketRepository ticketRepository;
    private final MastersRepository mastersRepository;
    private final TicketResponseRepository ticketResponseRepository;
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
                            dto.getQuery(),
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

    @Override
    public ApiResponseDTO<PagedResponse<StaffTicketResponseDTO>> staffTicketReport(
            StaffTicketRequestDTO requestDto) {
        log.info("<<START>> staffTicketReport <<START>>");
        log.info("Request received: {}", requestDto);
        try {
            log.debug("Fetching tickets from repository");
            List<Ticket> tickets = ticketRepository.findTicketsForStaffReport(
                    requestDto.getQuery(),
                    requestDto.getFromDate() != null ? requestDto.getFromDate().atStartOfDay() : null,
                    requestDto.getToDate() != null ? requestDto.getToDate().atTime(23, 59, 59) : null,
                    requestDto.getBranchId(),
                    requestDto.getIsActive()
            );
            log.info("Total tickets fetched: {}", tickets.size());
            Map<Users, List<Ticket>> ticketsByUser =
                    tickets.stream().collect(Collectors.groupingBy(Ticket::getAssignedTo));
            List<StaffTicketResponseDTO> content = new ArrayList<>();
            for (Map.Entry<Users, List<Ticket>> entry : ticketsByUser.entrySet()) {
                Users user = entry.getKey();
                List<Ticket> userTickets = entry.getValue();
                log.debug("Processing user: {}, ticketCount={}",
                        user.getUserName(), userTickets.size());
                int totalAssigned = userTickets.size();
                int openTickets = (int) userTickets.stream()
                        .filter(t -> t.getStatus() == 1)
                        .count();
                int closedTickets = (int) userTickets.stream()
                        .filter(t -> t.getStatus() == 2)
                        .count();
                List<TicketResponse> responses =
                        ticketResponseRepository.findByTicketIn(userTickets);
                int totalReplies = responses.size();
                Double avgReplyHours = responses.stream()
                        .filter(r -> r.getResponseTimeHours() != null)
                        .mapToDouble(TicketResponse::getResponseTimeHours)
                        .average()
                        .orElse(0.0);
                avgReplyHours = Math.round(avgReplyHours * 100.0) / 100.0;
                content.add(new StaffTicketResponseDTO(
                        user.getUserName(),
                        totalAssigned,
                        openTickets,
                        closedTickets,
                        totalReplies,
                        avgReplyHours
                ));
            }
            log.info("Staff ticket report prepared, total users: {}", content.size());
            int pageNumber = 0;
            int pageSize = content.size();
            long totalElements = content.size();
            int totalPages = totalElements > 0 ? 1 : 0;
            boolean isLast = true;
            PagedResponse<StaffTicketResponseDTO> pagedResponse =
                    new PagedResponse<>(
                            content,
                            pageNumber,
                            totalElements,
                            totalPages,
                            pageSize,
                            isLast
                    );
            log.info("<<END>> staffTicketReport completed successfully <<END>>");
            return new ApiResponseDTO<>(
                    pagedResponse,
                    "Staff ticket report fetched successfully",
                    HttpStatus.OK,
                    false
            );
        } catch (Exception e) {
            log.error("Error occurred in staffTicketReport", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }


    @Override
    public void exportResponsePenaltyReport(
            ResponsePenaltyRequestDTO dto,
            String format,
            HttpServletResponse response
    ) {

        log.info("<<START>> exportResponsePenaltyReport <<START>>");

        if (format == null ||
                !List.of("excel", "csv", "pdf")
                        .contains(format.toLowerCase())) {
            throw new IllegalArgumentException("Invalid export format");
        }

        try {
            Pageable pageable = Pageable.unpaged();

            Page<Ticket> pageResult =
                    ticketRepository.searchResponsePenaltyReport(
                            dto.getQuery(),
                            pageable
                    );

            if (pageResult.isEmpty()) {
                throw new RuntimeException("No data found for export");
            }

            List<ResponsePenaltyResponseDTO> data =
                    pageResult.getContent()
                            .stream()
                            .map(this::mapToResponsePenaltyDto)
                            .collect(Collectors.toList());

            ResponsePenaltyExportHelper.export(data, format, response);

            log.info(
                    "Response Penalty export completed | records={}",
                    data.size()
            );

        } catch (Exception e) {
            log.error("exportResponsePenaltyReport failed", e);
            throw new RuntimeException(
                    "Failed to export response penalty report", e
            );
        }

        log.info("<<END>> exportResponsePenaltyReport <<END>>");
    }


}
