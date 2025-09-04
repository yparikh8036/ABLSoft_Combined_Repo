package com.example.ablsoft.rest.errors;

import com.example.ablsoft.service.errors.ErrorConstants;
import com.example.ablsoft.service.errors.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler {

    public static final String ERROR_MESSAGE = "ERROR_MESSAGE";

    private final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<CustomExceptionVM> globalExceptionHandler(GlobalException e) {
        log.error("GlobalException Occurred", e);
        return new ResponseEntity<>(new CustomExceptionVM(e.getMessage(), e.getApplicationStatusCode(), e.getHttpStatus().value()), e.getHttpStatus());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<CustomExceptionVM> nullPointerExceptionHandler(NullPointerException e) {
        log.error("NullPointerException Occurred", e);
        return new ResponseEntity<>(new CustomExceptionVM(ERROR_MESSAGE, ErrorConstants.NULL_POINTER_EXCEPTION_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<CustomExceptionVM> dataAccessExceptionHandler(DataAccessException e) {
        log.error("DataAccessException Occurred", e);
        return new ResponseEntity<>(new CustomExceptionVM(ERROR_MESSAGE, ErrorConstants.DATA_ACCESS_EXCEPTION_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({SQLException.class, DataIntegrityViolationException.class})
    public ResponseEntity<CustomExceptionVM> sqlExceptionHandler(DataIntegrityViolationException e, SQLException e1) {
        log.error("SQLException | DataIntegrityViolationException Occurred : {}", e, e1);
        return new ResponseEntity<>(new CustomExceptionVM(ERROR_MESSAGE, ErrorConstants.SQL_EXCEPTION_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomExceptionVM> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String fields = e.getBindingResult().getFieldErrors().stream().map(FieldError::getField).collect(Collectors.joining(","));
        String msg = String.format("Validation Failed for %s fields", fields);
        return new ResponseEntity<>(new CustomExceptionVM(msg, ErrorConstants.METHOD_ARGUMENT_NOT_VALID_EXCEPTION_CODE,
                HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

}
