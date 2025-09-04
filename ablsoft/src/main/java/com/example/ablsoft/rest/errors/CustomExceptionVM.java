package com.example.ablsoft.rest.errors;

public class CustomExceptionVM {

    private String message;
    private int applicationStatusCode;
    private int httpStatus;

    public CustomExceptionVM(String message, int applicationStatusCode, int httpStatus) {
        this.message = message;
        this.applicationStatusCode = applicationStatusCode;
        this.httpStatus = httpStatus;
    }

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

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
        return "CustomExceptionVM{" +
                "message='" + message + '\'' +
                ", applicationStatusCode=" + applicationStatusCode +
                ", httpStatus=" + httpStatus +
                '}';
    }
}
