package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.Masters;
import com.sipl.ticket.core.dao.repository.MastersRepository;
import com.sipl.ticket.core.dao.repository.TicketRepository;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ChartDataResponseDTO;
import com.sipl.ticket.core.dto.response.ChartDataResponseDTO;
import com.sipl.ticket.core.dto.response.ChartItemDTO;
import com.sipl.ticket.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {
    private final TicketRepository ticketRepository;
    private final MastersRepository mastersRepository;


    @Override
    public ApiResponseDTO<ChartDataResponseDTO> getTicketStatus() {
        log.info("Fetching Ticket Status KPI...");

        try {
            Integer columnId = 2; // Status
            List<ChartItemDTO> items = ticketRepository.getTicketsByStatus(columnId);

            log.debug("Fetched {} status items from DB", items.size());

            List<String> labels = items.stream().map(ChartItemDTO::getLabel).collect(Collectors.toList());
            List<Long> series = items.stream().map(ChartItemDTO::getSeries).collect(Collectors.toList());

            log.debug("Status labels: {}", labels);
            log.debug("Status series: {}", series);

            ChartDataResponseDTO dto = new ChartDataResponseDTO(labels, series);

            return new ApiResponseDTO<>(
                    dto,
                    null,
                    null,
                    "Ticket status summary fetched successfully",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception e) {
            log.error("Error fetching ticket status KPI", e);
            return new ApiResponseDTO<>(
                    null,
                    null,
                    null,
                    "Failed to fetch ticket status summary",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true,
                    null,
                    null
            );
        }
    }

    @Override
    public ApiResponseDTO<ChartDataResponseDTO> getTicketPriorityStatus() {
        log.info("Fetching Ticket Priority KPI...");

        try {
            Integer columnId = 3; // Priority
            List<ChartItemDTO> items = ticketRepository.getTicketsByPriority(columnId);

            log.debug("Fetched {} priority items from DB", items.size());

            List<String> labels = items.stream().map(ChartItemDTO::getLabel).collect(Collectors.toList());
            List<Long> series = items.stream().map(ChartItemDTO::getSeries).collect(Collectors.toList());

            log.debug("Priority labels: {}", labels);
            log.debug("Priority series: {}", series);

            ChartDataResponseDTO dto = new ChartDataResponseDTO(labels, series);

            return new ApiResponseDTO<>(
                    dto,
                    null,
                    null,
                    "Ticket priority summary fetched successfully",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception e) {
            log.error("Error fetching ticket priority KPI", e);
            return new ApiResponseDTO<>(
                    null,
                    null,
                    null,
                    "Failed to fetch ticket priority summary",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true,
                    null,
                    null
            );
        }
    }

}

