package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO<T> implements Serializable {
    private T data;
    private List<T> dataList;
    private String requestId;
    private String message;
    private HttpStatus status;
    private boolean isError;
    private LocalDateTime timestamp = LocalDateTime.now();
    private List<ErrorDetail> errors;
    
    public ApiResponseDTO(T data, String message, HttpStatus status, boolean isError) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.isError = isError;
    }

    public ApiResponseDTO(T data, String requestId, String message, HttpStatus status, boolean isError) {
        this.data = data;
        this.requestId = requestId;
        this.message = message;
        this.status = status;
        this.isError = isError;
    }

    public ApiResponseDTO(String requestId, String message, HttpStatus status, boolean isError) {
        this.requestId = requestId;
        this.message = message;
        this.status = status;
        this.isError = isError;
    }

    public ApiResponseDTO(String message, HttpStatus status, boolean isError) {
        this.message = message;
        this.status = status;
        this.isError = isError;
    }

    public ApiResponseDTO(T data, String message, HttpStatus status, boolean isError, List<ErrorDetail> errors) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.isError = isError;
        this.errors = errors;
    }

    public ApiResponseDTO(String requestId, String message, HttpStatus status, boolean isError, List<ErrorDetail> errors) {
        this.requestId = requestId;
        this.message = message;
        this.status = status;
        this.isError = isError;
        this.errors = errors;
    }

    public ApiResponseDTO(String message, boolean isError, List<ErrorDetail> errors, HttpStatus status) {
        this.message = message;
        this.isError = isError;
        this.errors = errors;
        this.status = status;
    }
    public ApiResponseDTO(
            List<T> dataList, HttpStatus status, String message, boolean error, LocalDateTime timestamp) {
        this.dataList = dataList;
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = timestamp;
    }
}
