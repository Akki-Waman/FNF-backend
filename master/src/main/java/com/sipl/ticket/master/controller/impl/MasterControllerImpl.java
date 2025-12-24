package com.sipl.ticket.master.controller.impl;


import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.MastersResponseDTO;
import com.sipl.ticket.master.controller.MasterController;
import com.sipl.ticket.master.service.MasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MasterControllerImpl implements MasterController {

    private final MasterService masterService;

    @Override
    public ResponseEntity<ApiResponseDTO<List<MastersResponseDTO>>> findByColumnCode(Integer columnCode) {
        return ResponseEntity.ok(masterService.findByColumnCode(columnCode));

    }
}
