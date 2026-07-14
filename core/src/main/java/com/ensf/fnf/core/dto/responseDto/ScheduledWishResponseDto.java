package com.ensf.fnf.core.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledWishResponseDto {
    @JsonProperty("wish_id")
    private Long scheduledWishId;

    @JsonProperty("receiver_name")
    private String receiverName;

    @JsonProperty("relationship_type")
    private String relationshipType;

    @JsonProperty("wish_message")
    private String wishMessage;

    @JsonProperty("scheduled_time")
    private LocalDateTime scheduledDateTime;

    @JsonProperty("status")
    private String wishStatus;
}