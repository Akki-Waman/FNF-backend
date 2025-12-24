package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AndroidApiResponseDTO {
    private Boolean versionChanged;
    private String url;
    private String newVersion;
    private HttpStatus status;
    private String message;
}
