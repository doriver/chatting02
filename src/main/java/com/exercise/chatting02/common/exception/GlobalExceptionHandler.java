package com.exercise.chatting02.common.exception;

import com.exercise.chatting02.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpectedException.class)
    public ResponseEntity<ApiResponse> handleExpectedException(ExpectedException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 응답 헤더에는 400 코드가 가되,
                .body(ApiResponse.error(errorCode)); // 실제 상태 코드는 ErrorCode의 상태 코드이다.
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
            builder.append("\n");
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, builder.toString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnExpectedException(Exception ex) {
        log.error("에러 발생 :", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."));
    }
}