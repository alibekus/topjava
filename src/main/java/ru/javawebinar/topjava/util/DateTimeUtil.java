package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeUtil.class);

    private DateTimeUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <T extends Comparable<T>> boolean isBetween(T lt, T startTime, T endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }


    public static List<LocalDate> convertToDate(String dateFromString, String dateToString) {
        LocalDate from = LocalDate.MIN;
        LocalDate to = LocalDate.MAX;
        if (dateFromString.length() != 0) {
            from = LocalDate.parse(dateFromString);
        }
        if (dateToString.length() != 0) {
            to = LocalDate.parse(dateToString);
        }
        return Arrays.asList(from, to);
    }

    public static List<LocalTime> convertToTime(String timeFromString, String timeToString) {
        LocalTime timeFrom = LocalTime.of(0, 0);
        LocalTime timeTo = LocalTime.of(23, 59);
        if (timeFromString.length() != 0) {
            timeFrom = LocalTime.parse(timeFromString);
        }
        if (timeToString.length() != 0) {
            timeTo = LocalTime.parse(timeToString);
        }
        return Arrays.asList(timeFrom, timeTo);
    }

}

