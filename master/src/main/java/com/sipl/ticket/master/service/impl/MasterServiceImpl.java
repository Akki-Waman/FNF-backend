package com.sipl.ticket.master.service.impl;

import com.sipl.ticket.core.dao.entity.Masters;
import com.sipl.ticket.core.dao.repository.MastersRepository;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.MastersResponseDTO;
import com.sipl.ticket.core.mapper.MastersMapper;
import com.sipl.ticket.master.service.MasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.cache.annotation.Cacheable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {


    private final MastersRepository mastersRepository;
    private final MastersMapper mastersMapper;

    @Override
    public ApiResponseDTO<List<MastersResponseDTO>> findByColumnCode(Integer columnCode) {
        try {
            log.info("Fetching masters by columnCode: {}", columnCode);

            if (columnCode == null) {
                log.warn("ColumnCode is null");
                return new ApiResponseDTO<>(
                        null,
                        "Details not found",
                        HttpStatus.NOT_FOUND,
                        false
                );
            }

            List<Masters> mastersList = mastersRepository.findByColumnCode(columnCode);
            List<MastersResponseDTO> dtos = mastersMapper.toDtoList(mastersList);

            if (dtos.isEmpty()) {
                log.warn("No masters found for columnCode: {}", columnCode);
                return new ApiResponseDTO<>(
                        null,
                        "Details not found",
                        HttpStatus.NOT_FOUND,
                        false
                );
            }

            return new ApiResponseDTO<>(
                    dtos,
                    "Master details found",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("Exception in findByColumnCode for columnCode: {}", columnCode, e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @Cacheable("ticketPriorityMap")
    public Map<Integer, String> getTicketPriorityMap() {
        log.debug("Loading Ticket Priority master data");
        return getMasterMap("ticket", 3);
    }


    @Override
    @Cacheable("ticketStatusMap")
    public Map<Integer, String> getTicketStatusMap() {
        log.info("Loading Ticket Status master data");
        return getMasterMap("ticket", 2);
    }

    @Override
    @Cacheable("taskStatusMap")
    public Map<Integer, String> getTaskStatusMap() {
        log.info("Loading Task Status master data");
        return getMasterMap("tasks", 1);
    }

    @Override
    @Cacheable("taskPriorityMap")
    public Map<Integer, String> getTaskPriorityMap() {
        log.info("Loading Task Priority master data");
        return getMasterMap("tasks", 6);
    }

    private Map<Integer, String> getMasterMap(String tblName, Integer columnCode) {

        return mastersRepository
                .findActiveMasters(tblName, columnCode)
                .stream()
                .filter(r -> r[0] != null && r[1] != null)
                .collect(Collectors.toMap(
                        r -> (Integer) r[0],
                        r -> (String) r[1],
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }


}


