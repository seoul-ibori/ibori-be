package com.springboot.iboribe.global.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.springboot.iboribe.domain.auth.exception.AuthErrorCode;
import com.springboot.iboribe.global.common.BaseResponse;
import com.springboot.iboribe.global.exception.model.BaseErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /** 커스텀 예외 */
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<BaseResponse<Object>> handleCustomException(CustomException ex) {
    BaseErrorCode errorCode = ex.getErrorCode();
    log.error("Custom 오류 발생: {}", ex.getMessage());
    return ResponseEntity.status(errorCode.getStatus())
        .body(BaseResponse.error(errorCode.getStatus().value(), ex.getMessage()));
  }

  /** Validation 실패 */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BaseResponse<Object>> handleValidationException(
      MethodArgumentNotValidException ex) {

    String errorMessages =
        ex.getBindingResult().getFieldErrors().stream()
            .map(e -> String.format("[%s] %s", e.getField(), e.getDefaultMessage()))
            .collect(Collectors.joining(" / "));

    log.warn("Validation 오류 발생: {}", errorMessages);

    return ResponseEntity.status(AuthErrorCode.INVALID_INPUT_FORMAT.getStatus())
        .body(
            BaseResponse.error(
                AuthErrorCode.INVALID_INPUT_FORMAT.getStatus().value(),
                AuthErrorCode.INVALID_INPUT_FORMAT.getMessage()));
  }

  /** JSON 파싱 실패 (body 형식 틀림) */
  @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
  public ResponseEntity<BaseResponse<Object>> handleHttpMessageNotReadableException(Exception ex) {

    log.warn("JSON 파싱 오류: {}", ex.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(BaseResponse.error(400, "요청 형식이 올바르지 않습니다."));
  }

  /** 파라미터 타입 오류 (ex: Long인데 String 들어옴) */
  @ExceptionHandler(
      org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
  public ResponseEntity<BaseResponse<Object>> handleTypeMismatch(Exception ex) {

    log.warn("타입 불일치 오류: {}", ex.getMessage());

    return ResponseEntity.badRequest().body(BaseResponse.error(400, "요청 파라미터 타입이 올바르지 않습니다."));
  }

  /** 권한 없음 */
  @ExceptionHandler(org.springframework.security.authorization.AuthorizationDeniedException.class)
  public ResponseEntity<BaseResponse<Object>> handleAccessDenied(Exception ex) {

    log.warn("권한 없음: {}", ex.getMessage());

    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(BaseResponse.error(403, "접근 권한이 없습니다."));
  }

  /** 예상치 못한 서버 오류 */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseResponse<Object>> handleException(Exception ex) {

    log.error("Server 오류 발생: ", ex);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(BaseResponse.error(500, "예상치 못한 서버 오류가 발생했습니다."));
  }
}
