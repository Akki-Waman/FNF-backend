package com.ensf.fnf.core.dto.requestDto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ScheduleWishRequestDto {
    @NotNull(message = "Receiver reference identifier is mandatory")
    private Long receiverMemberId;

    @NotBlank(message = "Wish message text block cannot be empty")
    private String wishMessage;

    @NotNull(message = "Execution runtime target timestamp is mandatory")
    private LocalDateTime scheduledDateTime;

    private String mediaTemplateUrl;
}