package com.sipl.ticket.master.service;

import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.MastersResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface MasterService {
    public ApiResponseDTO<List<MastersResponseDTO>> findByColumnCode(Integer columnCode);

    Map<Integer, String> getTicketStatusMap();

    Map<Integer, String> getTicketPriorityMap();

    Map<Integer, String> getTaskStatusMap();

    Map<Integer, String> getTaskPriorityMap();


}

