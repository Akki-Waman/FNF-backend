package com.sipl.ticket.core.exception.custom;

import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ErrorDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Handle Entity Not Found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleEntityNotFound(EntityNotFoundException ex) {
        ApiResponseDTO<Object> response = new ApiResponseDTO<>(
                null,
                 ex.getMessage(),
                HttpStatus.NOT_FOUND,
                true
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    // Handle IllegalArgumentException (optional case)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        ApiResponseDTO<Object> response = new ApiResponseDTO<>(
                null,
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                true
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle Generic Exception (fallback for ANY exception)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleGenericException(Exception ex) {
        ApiResponseDTO<Object> response = new ApiResponseDTO<>(
                null,
                 ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                true
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FieldAlreadyExistException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleFieldAlreadyExistException(FieldAlreadyExistException ex) {
        log.error("GlobalExceptionHandler: FieldAlreadyExistException: ", ex);
        return ResponseEntity.ok(new ApiResponseDTO<>(ex.getMessage(), HttpStatus.CONFLICT, true));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleNoSuchElementException(NoSuchElementException ex) {
        log.error("GlobalExceptionHandler: NoSuchElementException: ", ex);
        return ResponseEntity.ok(new ApiResponseDTO<>(ex.getMessage(), HttpStatus.NOT_FOUND, true));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseDTO<Object>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.info("GlobalExceptionHandler: MethodArgumentNotValidException: ", ex);
        List<ErrorDetail> errorDetails = ex.getBindingResult().getFieldErrors().stream().map(error -> new ErrorDetail(error.getCode(), error.getDefaultMessage(), error.getField())).collect(Collectors.toList());

        ApiResponseDTO<Object> response = new ApiResponseDTO<>();
        response.setData(null);
        if (request.getHeader("X-Request-Id") != null) {
            response.setRequestId(request.getHeader("X-Request-Id"));
        }
        response.setMessage("Validation failed");
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setError(true);
        response.setErrors(errorDetails);

        return ResponseEntity.ok(response);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {

        List<ErrorDetail> errorDetails = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            ErrorDetail errorDetail = new ErrorDetail();
            errorDetail.setCode("ERR_VALIDATION");
            errorDetail.setDetail(violation.getMessage());
            errorDetail.setSource(violation.getPropertyPath().toString());
            errorDetails.add(errorDetail);
        }

        ApiResponseDTO<Object> response = new ApiResponseDTO<>();
        if (request.getHeader("X-Request-Id") != null) {
            response.setRequestId(request.getHeader("X-Request-Id"));
        }
        response.setMessage("Validation failed for one or more fields.");
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setError(true);
        response.setTimestamp(LocalDateTime.now());
        response.setErrors(errorDetails);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {

        String detailMsg = (ex.getRootCause() != null) ? ex.getRootCause().getMessage() : ex.getMessage();

        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setCode("ERR_UNIQUE_CONSTRAINT");
        errorDetail.setDetail("A unique constraint was violated. " + (detailMsg != null ? detailMsg.trim() : ""));
        errorDetail.setSource(extractFieldFromDbMessage(detailMsg));

        List<ErrorDetail> errorDetails = new ArrayList<>();
        errorDetails.add(errorDetail);
        ApiResponseDTO<Object> response = new ApiResponseDTO<>();
        if (request.getHeader("X-Request-Id") != null) {
            response.setRequestId(request.getHeader("X-Request-Id"));
        }
        response.setMessage("Cannot delete this record. It is linked to the another table");
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setError(true);
        response.setErrors(errorDetails);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private String extractFieldFromDbMessage(String dbMessage) {
        if (dbMessage != null && dbMessage.contains("Detail: Key")) {
            int start = dbMessage.indexOf('(');
            int end = dbMessage.indexOf(')');
            if (start != -1 && end != -1 && end > start) {
                return dbMessage.substring(start + 1, end);
            }
        }
        return null;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<?>> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiResponseDTO<?> response = new ApiResponseDTO<>();
        response.setMessage(ex.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setError(true);
        response.setTimestamp(java.time.LocalDateTime.now()); // You already have default value, but it's explicit here
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleIllegalStateException(IllegalStateException ex) {
        log.error("GlobalExceptionHandler: IllegalStateException: ", ex);

        ApiResponseDTO<Object> response = new ApiResponseDTO<>();
        response.setData(null);
        response.setMessage(ex.getMessage() != null ? ex.getMessage() : "Illegal state encountered");
        response.setStatus(HttpStatus.CONFLICT);
        response.setError(true);
        response.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

}

