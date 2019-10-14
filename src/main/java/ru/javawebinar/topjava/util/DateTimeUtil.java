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
    private static final Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);
    private DateTimeUtil() { throw new IllegalStateException("Utility class");}

    public static boolean isBetween(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }

    public static boolean isBetween(LocalDateTime ldt, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ldt.compareTo(startDateTime) >= 0 && ldt.compareTo(endDateTime) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }


    public static List<LocalDate> convertToDates(String dateFromString, String dateToString) {
        LocalDate from = LocalDate.MIN;
        LocalDate to = LocalDate.MAX;
        if (dateFromString != "") {
            from = convertToDate(dateFromString);
        }
        if (dateToString != "") {
            to = convertToDate(dateToString);
        }
        return Arrays.asList(from,to);
    }

    public static List<LocalTime> convertToTime(String timeFromString, String timeToString) {
        LocalTime timeFrom = LocalTime.of(0,0);
        LocalTime timeTo = LocalTime.of(23,59);
        if (timeFromString.length() != 0) {
            timeFrom = convertToTime(timeFromString);
        }
        if (timeToString.length() != 0) {
            timeTo = convertToTime(timeToString);
        }
        return Arrays.asList(timeFrom,timeTo);
    }

    private static LocalTime convertToTime(String timeString) {
        int hour = 0;
        int min = 0;
        String hourString = timeString.substring(0, 2);
        if (hourString.length() != 0) {
            hour = Integer.parseInt(hourString);
        }
        String minString = timeString.substring(3, 5);
        if (minString.length() != 0) {
            min = Integer.parseInt(minString);
        }
        return LocalTime.of(hour,min);
    }

    private static LocalDate convertToDate(String dateString) {
        int year = 0;
        int month = 0;
        int day = 0;
        String yearString = dateString.substring(0, 4);
        if (yearString.length() != 0) {
            year = Integer.parseInt(yearString);
        }
        String monthString = dateString.substring(5, 7);
        if (monthString.length() != 0) {
            month = Integer.parseInt(monthString);
        }
        String dayString = dateString.substring(8, 10);
        if (dayString.length() != 0) {
            day = Integer.parseInt(dayString);
        }
        return LocalDate.of(year,month,day);
    }
}

