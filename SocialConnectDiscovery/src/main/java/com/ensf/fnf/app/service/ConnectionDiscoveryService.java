package com.ensf.fnf.app.service;

import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import java.util.List;

public interface ConnectionDiscoveryService {
    CommonApiResponse<List<Object>> fetchSuggestedConnections();
    CommonApiResponse<Object> fetchPendingRequestMatrices();
}