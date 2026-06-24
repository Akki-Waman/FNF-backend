package com.ensf.fnf.core.exception;

import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(
            FnfException.class
    )
    public ResponseEntity<
            CommonApiResponse<Object>>
    handleFnfException(
            FnfException ex) {

        log.error(
                ex.getMessage(),
                ex
        );

        return ResponseEntity
                .badRequest()
                .body(
                        CommonApiResponse
                                .builder()
                                .success(false)
                                .message(
                                        ex.getMessage()
                                )
                                .build()
                );
    }
}

