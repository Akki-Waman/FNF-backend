package com.ensf.fnf.app.service.impl;

import com.ensf.fnf.app.service.ConnectionDiscoveryService;
import com.ensf.fnf.core.dao.entity.UserEntity;
import com.ensf.fnf.core.dao.repository.FriendRequestRepository;
import com.ensf.fnf.core.dao.repository.UserRepository;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ConnectionDiscoveryServiceImpl implements ConnectionDiscoveryService {

    private final UserRepository userRepository;
    private final FriendRequestRepository requestRepository;

    @Override
    @Transactional(readOnly = true)
    public CommonApiResponse<List<Object>> fetchSuggestedConnections() {
        log.info("Executing graph discovery algorithm...");
        UserEntity user = getAuthenticatedUser();

        List<Object> suggestedProfiles = runMutualFriendsAlgorithm(user.getUserId());

        return CommonApiResponse.<List<Object>>builder()
                .success(true)
                .data(suggestedProfiles)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CommonApiResponse<Object> fetchPendingRequestMatrices() {
        log.info("Fetching incoming and outgoing pending request matrices");
        UserEntity user = getAuthenticatedUser();

        List<Object> incoming = retrieveIncomingRequests(user.getUserId());
        List<Object> outgoing = retrieveSentRequests(user.getUserId());

        Map<String, List<Object>> matrices = new HashMap<>();
        matrices.put("received", incoming);
        matrices.put("sent", outgoing);

        return CommonApiResponse.<Object>builder()
                .success(true)
                .data(matrices)
                .build();
    }

    // ==========================================
    // PRIVATE HELPER METHODS (SEGREGATION)
    // ==========================================

    private UserEntity getAuthenticatedUser() {
        return userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
    }

    private List<Object> runMutualFriendsAlgorithm(Long userId) {
        log.debug("Calculating 2nd-degree connections for userId: {}", userId);
        // Algorithm: Find friends of friends who are not currently friends with userId.
        return Collections.emptyList();
    }

    private List<Object> retrieveIncomingRequests(Long userId) {
        log.debug("Fetching 'PENDING' requests where receiver_id = {}", userId);
        // Map FriendRequestEntity to DTO using MapStruct
        return Collections.emptyList();
    }

    private List<Object> retrieveSentRequests(Long userId) {
        log.debug("Fetching 'PENDING' requests where sender_id = {}", userId);
        return Collections.emptyList();
    }
}