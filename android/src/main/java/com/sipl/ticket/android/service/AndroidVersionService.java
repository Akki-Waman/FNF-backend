package com.sipl.ticket.android.service;

import com.sipl.ticket.core.dto.response.AndroidApiResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AndroidVersionService {

    public AndroidApiResponseDTO checkVersion(String appId, String version);
    public ResponseEntity<Resource> downloadFile(String fileName);
}
