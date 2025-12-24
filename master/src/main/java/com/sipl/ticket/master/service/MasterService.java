package com.sipl.ticket.master.service;

import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.MastersResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MasterService {
    public ApiResponseDTO<List<MastersResponseDTO>> findByColumnCode(Integer columnCode);
}

