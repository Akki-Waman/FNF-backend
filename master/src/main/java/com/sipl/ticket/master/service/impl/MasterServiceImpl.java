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

import java.util.List;


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

}

