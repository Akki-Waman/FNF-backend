package com.ensf.fnf.app.controller.impl;

import com.ensf.fnf.app.controller.ConnectionDiscoveryController;
import com.ensf.fnf.app.service.ConnectionDiscoveryService;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ConnectionDiscoveryControllerImpl implements ConnectionDiscoveryController {

    private final ConnectionDiscoveryService connectionService;

    @Override
    public ResponseEntity<CommonApiResponse<List<Object>>> discoverPeople() {
        log.info("<<START>> ConnectionControllerImpl :: discoverPeople <<START>>");
        CommonApiResponse<List<Object>> response = connectionService.fetchSuggestedConnections();
        log.info("<<END>> ConnectionControllerImpl :: discoverPeople <<END>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommonApiResponse<Object>> getPendingRequests() {
        log.info("<<START>> ConnectionControllerImpl :: getPendingRequests <<START>>");
        CommonApiResponse<Object> response = connectionService.fetchPendingRequestMatrices();
        log.info("<<END>> ConnectionControllerImpl :: getPendingRequests <<END>>");
        return ResponseEntity.ok(response);
    }
}