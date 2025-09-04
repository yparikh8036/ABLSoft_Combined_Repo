package com.example.ablsoft.service.errors;

public class ErrorConstants {

    private ErrorConstants() {
        // ErrorConstants
    }

    public static final int DUPLICATE_INVOICE_EXCEPTION_CODE = 1000;
    public static final int NULL_POINTER_EXCEPTION_CODE = 1002;
    public static final int DATA_ACCESS_EXCEPTION_CODE = 1003;
    public static final int SQL_EXCEPTION_CODE = 1004;
    public static final int METHOD_ARGUMENT_NOT_VALID_EXCEPTION_CODE = 1005;
    public static final int CSV_PROCESSING_EXCEPTION_CODE = 1006;


    public static final String DUPLICATE_INVOICE_EXCEPTION_MESSAGE = "Duplicate Invoice";
    public static final String CSV_PROCESSING_EXCEPTION_MESSAGE = "Error while processing CSV file: ";
}
