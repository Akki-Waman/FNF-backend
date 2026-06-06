package com.ensf.fnf.core.dto.responseDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class LoginOtpResponse {

    private UUID requestId;
    private List<String> sentTo;
    private OffsetDateTime expiresAt;
    private String demoOtp;

    public LoginOtpResponse(UUID requestId, List<String> sentTo, OffsetDateTime expiresAt, String demoOtp) {
        this.requestId = requestId;
        this.sentTo = sentTo;
        this.expiresAt = expiresAt;
        this.demoOtp = demoOtp;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public List<String> getSentTo() {
        return sentTo;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public String getDemoOtp() {
        return demoOtp;
    }
}
