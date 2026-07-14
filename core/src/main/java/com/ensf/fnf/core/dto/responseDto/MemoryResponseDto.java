package com.ensf.fnf.core.dto.responseDto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoryResponseDto {
    private Long memoryId;
    private String mediaUrl;
    private String mediaType;
    private String caption;
    private String albumName;
    private LocalDateTime createdAt;
}