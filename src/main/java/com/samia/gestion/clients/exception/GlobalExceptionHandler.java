package com.samia.gestion.clients.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody Map<String, String> getExceptionHandler(MethodArgumentNotValidException exception){
        exception.printStackTrace();
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;

    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({NotFoundException.class})
    public  @ResponseBody ErrorDetails notHoundException(NotFoundException exception) {
        exception.printStackTrace();
        return new ErrorDetails(exception.getMessage(), "NOT_FOUND");
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler({AlreadyExistsException.class})
    public  @ResponseBody ErrorDetails alreadyExistsException(AlreadyExistsException exception) {
        exception.printStackTrace();
        return new ErrorDetails(exception.getMessage(), "ALREADY_EXISTS");
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(OtherExceptions.class)
    public @ResponseBody ErrorDetails handleOtherException(OtherExceptions exception) {
        exception.printStackTrace();
        return new ErrorDetails(exception.getMessage(), "BAD_REQUEST");
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public @ResponseBody ErrorDetails handleUnauthorizedException(UnauthorizedException exception) {
        exception.printStackTrace();
        return new ErrorDetails( exception.getMessage(), "UNAUTHORIZED");
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public @ResponseBody ErrorDetails handleGenericException(Exception exception) {
        exception.printStackTrace();
        return new ErrorDetails("An unexpected error occurred.", "INTERNAL_SERVER_ERROR");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public @ResponseBody ErrorDetails handleAccessDeniedException(AccessDeniedException exception) {
        exception.printStackTrace();
        return new ErrorDetails("Access denied: " + exception.getMessage(), "FORBIDDEN");
    }

}
