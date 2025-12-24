package com.sipl.ticket.android.controller.impl;

import com.sipl.ticket.android.controller.AndroidVersionController;
import com.sipl.ticket.android.service.AndroidVersionService;
import com.sipl.ticket.core.dto.response.AndroidApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AndroidVersionControllerImpl implements AndroidVersionController {

    private final AndroidVersionService androidVersionService;


    @Override
    public ResponseEntity<AndroidApiResponseDTO> checkVersion(String appId, String version) {
        return ResponseEntity.ok(androidVersionService.checkVersion(appId,version));
    }

    @Override
    public ResponseEntity<?> downloadFile(String fileName) {
        return androidVersionService.downloadFile(fileName);
    }
}
