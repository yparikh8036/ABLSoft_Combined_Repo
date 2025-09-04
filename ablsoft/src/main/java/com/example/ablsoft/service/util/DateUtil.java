package com.example.ablsoft.service.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    public static Long calculateAge(String dateStr) {
        // Parse the invoice date
        LocalDate invoiceDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);

        // Get today's date
        LocalDate today = LocalDate.now();

        // Calculate the difference in days
        return ChronoUnit.DAYS.between(invoiceDate, today);
    }
}
