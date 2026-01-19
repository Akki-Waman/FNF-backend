package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.Masters;
import com.sipl.ticket.core.dao.repository.MastersRepository;
import com.sipl.ticket.core.dao.repository.TicketRepository;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ChartSeriesDTO;
import com.sipl.ticket.core.dto.response.ChartSeriesDTO;
import com.sipl.ticket.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {
    private final TicketRepository ticketRepository;
    private final MastersRepository mastersRepository;


    @Override
    public ApiResponseDTO<ChartSeriesDTO> getTicketStatus() {
        log.info("Fetching Ticket Summary KPI...");
        try{
            List<Object[]> ticketCounts = ticketRepository.countTicketsByStatus();

            Map<String, Long> countMap = new HashMap<>();
            for (Object[] row : ticketCounts) {
                String statusName = row[0].toString();
                Long count = ((Number) row[1]).longValue();
                countMap.put(statusName, count);
            }

            List<Masters> statuses = mastersRepository.findAllActiveStatuses(2);

            List<String> labels = new ArrayList<>();
            List<Integer> series = new ArrayList<>();

            for (Masters master : statuses) {
                String statusLabel = master.getValueDesc();
                labels.add(statusLabel);
                series.add(countMap.getOrDefault(statusLabel, 0L).intValue());
            }

            ChartSeriesDTO statusDTO = new ChartSeriesDTO(
                    labels,
                    series
            );

            return new ApiResponseDTO<>(
                    statusDTO,
                    null,
                    null,
                    "Ticket summary fetched successfully",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception e) {
            log.error("Error fetching ticket summary KPI", e);
            return new ApiResponseDTO<>(
                    null,
                    null,
                    null,
                    "Failed to fetch ticket summary",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true,
                    null,
                    null
            );
        }
    }

    @Override
    public ApiResponseDTO<ChartSeriesDTO> getTicketPriorityStatus() {
        log.info("Fetching Ticket Priority KPI...");
        try {
            Integer columnId=3;
            List<Object[]> ticketCounts = ticketRepository.countTicketsByPriority(columnId);

            Map<String, Long> countMap = new HashMap<>();
            for (Object[] row : ticketCounts) {
                String priority = row[0].toString();
                Long count = ((Number) row[1]).longValue();
                countMap.put(priority, count);
            }

            List<Masters> priorities = mastersRepository.findAllActiveStatuses(3);

            List<String> lables = new ArrayList<>();
            List<Integer> series = new ArrayList<>();

            for (Masters master : priorities) {
                String label = master.getValueDesc();
                lables.add(label);
                series.add(countMap.getOrDefault(label, 0L).intValue());
            }

            ChartSeriesDTO dto = new ChartSeriesDTO(lables, series);

            return new ApiResponseDTO<>(dto, null, null,
                    "Ticket priority summary fetched successfully",
                    HttpStatus.OK, false, null, null);

        } catch (Exception e) {
            log.error("Error fetching ticket priority KPI", e);

            return new ApiResponseDTO<>(null, null, null,
                    "Failed to fetch ticket priority summary",
                    HttpStatus.INTERNAL_SERVER_ERROR, true, null, null);
        }
    }

}

