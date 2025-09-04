package com.example.ablsoft.service.errors;

import org.springframework.http.HttpStatus;

public class GlobalException extends RuntimeException {

    private String message;
    private int applicationStatusCode;
    private HttpStatus httpStatus;


    public GlobalException(String message, int applicationStatusCode, HttpStatus httpStatus){
        super(message);
        this.applicationStatusCode = applicationStatusCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getApplicationStatusCode() {
        return applicationStatusCode;
    }

    public void setApplicationStatusCode(int applicationStatusCode) {
        this.applicationStatusCode = applicationStatusCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
        return "GlobalException{" +
                "message='" + message + '\'' +
                ", applicationStatusCode=" + applicationStatusCode +
                ", httpStatus=" + httpStatus +
                '}';
    }
}
