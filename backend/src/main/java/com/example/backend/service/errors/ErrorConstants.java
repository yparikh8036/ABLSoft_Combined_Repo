package com.example.backend.service.errors;

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
    public static final int CSV_PROCESSING_NULL_HEADER_EXCEPTION_CODE = 1007;
    public static final int CSV_PROCESSING_NO_DELIMITER_EXCEPTION_CODE = 1008;
    public static final int CSV_PROCESSING_EMPTY_FILE_EXCEPTION_CODE = 1008;
    public static final int NEW_INVOICE_ID_EXCEPTION_CODE = 1009;


    public static final String DUPLICATE_INVOICE_EXCEPTION_MESSAGE = "Customer ID and Invoice Number already exists";
    public static final String CSV_PROCESSING_EXCEPTION_MESSAGE = "Error while processing CSV file: ";
    public static final String CSV_PROCESSING_NULL_HEADER_EXCEPTION_MESSAGE = "Error while processing CSV file, Null Header ";
    public static final String CSV_PROCESSING_NO_DELIMITER_EXCEPTION_MESSAGE = "Error while processing CSV file, No Delimiter ";
    public static final String CSV_PROCESSING_EMPTY_FILE_EXCEPTION_MESSAGE = "Error while processing CSV file, Empty File ";
    public static final String NEW_INVOICE_ID_EXCEPTION_MESSAGE = "A new Invoice cannot already have an ID";
}
