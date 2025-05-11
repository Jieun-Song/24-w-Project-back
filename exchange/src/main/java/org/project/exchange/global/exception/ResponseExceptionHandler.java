package org.project.exchange.global.exception;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

import org.project.exchange.global.api.ApiResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice(basePackages = "org.project.exchange")
public class ResponseExceptionHandler {
    /**
     * IllegalArgumentException, NoSuchElementException 예외 처리
     * 
     * @param e 예외
     * @return ApiResponse<?>
     */
    @ExceptionHandler({ IllegalArgumentException.class, NoSuchElementException.class })
    public ResponseEntity<ApiResponse<?>> handleCommonException(Exception e) {
        return ResponseEntity.badRequest().body(ApiResponse.createError(e.getMessage()));
    }

    /**
     * AccessDeniedException 예외 처리
     * 
     * @return ApiResponse<?>
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.createError("접근이 거부되었습니다."));
    }

    /**
     * DuplicateKeyException 예외 처리
     * 
     * @param exception 예외
     * @return ApiResponse<?>
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicatedUserException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.createError(exception.getMessage()));
    }

    /**
     * MethodArgumentNotValidException 예외 처리
     * 
     * @param bindingResult 유효하지 않은 데이터
     * @return ApiResponse<?>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(BindingResult bindingResult) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createFail(bindingResult));
    }

    /**
     * SignatureException 예외 처리
     * 
     * @param exception 예외
     * @return ApiResponse<?>
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<?>> handleResponseStatusException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(ApiResponse.createError(exception.getReason()));
    }

    /**
     * RuntimeException 예외 처리
     * 
     * @param exception 예외
     * @return ApiResponse<?>
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.createError(exception.getMessage()));
    }
}