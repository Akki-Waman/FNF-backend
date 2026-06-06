package com.ensf.fnf.core.exception;

import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    ResponseEntity<ApiErrorResponse> handleApiException(ApiException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(new ApiErrorResponse(exception.getMessage(), request.getRequestURI(), OffsetDateTime.now()));
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiErrorResponse handleBadRequest(Exception exception, HttpServletRequest request) {
        return new ApiErrorResponse(exception.getMessage(), request.getRequestURI(), OffsetDateTime.now());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonApiResponse<Object>> handleCustomException(CustomException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonApiResponse.builder()
                        .success(Boolean.FALSE)
                        .message(exception.getMessage())
                        .build());
    }
}
