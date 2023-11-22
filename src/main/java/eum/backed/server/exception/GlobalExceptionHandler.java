package eum.backed.server.exception;

import eum.backed.server.common.DTO.DataResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public DataResponse handleIllegalArguments(IllegalArgumentException exception){
        return new DataResponse().fail(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public DataResponse handlerExceptionHandler(ExpiredJwtException exception){
        return new DataResponse().fail(exception.getMessage(), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(NullPointerException.class)
    public DataResponse handlerNullPointerException(NullPointerException exception){
        return new DataResponse().fail(exception.getMessage(), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(IllegalAccessException.class)
    public DataResponse handlerIllegalAccessException(IllegalAccessException exception){
        return new DataResponse().fail(exception.getMessage(), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(TokenExpiredException.class)
    public DataResponse handlerTokenExpiredException(TokenExpiredException exception){
        return new DataResponse().fail(exception.getMessage(), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(RuntimeException.class)
    public DataResponse handlerNullPointerException(RuntimeException exception){
        return new DataResponse().fail(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

}
