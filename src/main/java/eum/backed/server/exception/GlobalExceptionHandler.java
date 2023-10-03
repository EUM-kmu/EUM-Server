package eum.backed.server.exception;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.common.DTO.Response;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public DataResponse<Response> handleIllegalArguments(IllegalArgumentException exception){
        return new DataResponse<>(Response.class).fail(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public DataResponse<Response> handlerExceptionHandler(ExpiredJwtException exception){
        return new DataResponse<>(Response.class).fail(exception.getMessage(), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(NullPointerException.class)
    public DataResponse<Response> handlerNullPointerException(ExpiredJwtException exception){
        return new DataResponse<>(Response.class).fail(exception.getMessage(), HttpStatus.FORBIDDEN);
    }


}
