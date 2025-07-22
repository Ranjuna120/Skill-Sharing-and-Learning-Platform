package com.example.skill_sharing_backend.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Arrays;

/**
 * Utility class for date and time operations
 * Provides various date formatting, parsing, and calculation methods
 */
public class DateUtils {
    
    // Common date format patterns
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_ONLY_PATTERN = "yyyy-MM-dd";
    public static final String TIME_ONLY_PATTERN = "HH:mm:ss";
    public static final String ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DISPLAY_PATTERN = "MMM dd, yyyy HH:mm";
    
    // Formatters for different patterns
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_ONLY_PATTERN);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_ONLY_PATTERN);
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern(ISO_PATTERN);
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern(DISPLAY_PATTERN);
    
    /**
     * Private constructor to prevent instantiation
     */
    private DateUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Get current timestamp as LocalDateTime
     * @return current timestamp
     */
    public static LocalDateTime getCurrentTimestamp() {
        return LocalDateTime.now();
    }
    
    /**
     * Format LocalDateTime to string using default pattern
     * @param dateTime the date time to format
     * @return formatted string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DEFAULT_FORMATTER) : null;
    }
    
    /**
     * Format LocalDateTime to string using custom pattern
     * @param dateTime the date time to format
     * @param pattern the pattern to use
     * @return formatted string
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null || pattern == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }
    
    /**
     * Format LocalDateTime for display purposes
     * @param dateTime the date time to format
     * @return display formatted string
     */
    public static String formatForDisplay(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DISPLAY_FORMATTER) : null;
    }
    
    /**
     * Get date only part from LocalDateTime
     * @param dateTime the date time
     * @return date only string
     */
    public static String getDateOnly(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_FORMATTER) : null;
    }
    
    /**
     * Get time only part from LocalDateTime
     * @param dateTime the date time
     * @return time only string
     */
    public static String getTimeOnly(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(TIME_FORMATTER) : null;
    }
    
    /**
     * Calculate difference between two dates in days
     * @param startDate start date
     * @param endDate end date
     * @return difference in days
     */
    public static long getDaysBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
    }
    
    /**
     * Calculate difference between two dates in hours
     * @param startDate start date
     * @param endDate end date
     * @return difference in hours
     */
    public static long getHoursBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.HOURS.between(startDate, endDate);
    }
    
    /**
     * Calculate difference between two dates in minutes
     * @param startDate start date
     * @param endDate end date
     * @return difference in minutes
     */
    public static long getMinutesBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.MINUTES.between(startDate, endDate);
    }
    
    /**
     * Check if a date is today
     * @param dateTime the date to check
     * @return true if date is today
     */
    public static boolean isToday(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return dateTime.toLocalDate().equals(now.toLocalDate());
    }
    
    /**
     * Check if a date is in the past
     * @param dateTime the date to check
     * @return true if date is in the past
     */
    public static boolean isPast(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.isBefore(LocalDateTime.now());
    }
    
    /**
     * Check if a date is in the future
     * @param dateTime the date to check
     * @return true if date is in the future
     */
    public static boolean isFuture(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.isAfter(LocalDateTime.now());
    }
    
    /**
     * Add days to a date
     * @param dateTime the base date
     * @param days number of days to add
     * @return new date with added days
     */
    public static LocalDateTime addDays(LocalDateTime dateTime, long days) {
        return dateTime != null ? dateTime.plusDays(days) : null;
    }
    
    /**
     * Add hours to a date
     * @param dateTime the base date
     * @param hours number of hours to add
     * @return new date with added hours
     */
    public static LocalDateTime addHours(LocalDateTime dateTime, long hours) {
        return dateTime != null ? dateTime.plusHours(hours) : null;
    }
    
    /**
     * Add minutes to a date
     * @param dateTime the base date
     * @param minutes number of minutes to add
     * @return new date with added minutes
     */
    public static LocalDateTime addMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime != null ? dateTime.plusMinutes(minutes) : null;
    }
    
    /**
     * Get time ago string representation
     * @param dateTime the date to compare
     * @return human readable time ago string
     */
    public static String getTimeAgo(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "Unknown";
        }
        
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(dateTime, now);
        
        if (minutes < 1) {
            return "Just now";
        } else if (minutes < 60) {
            return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        }
        
        long hours = ChronoUnit.HOURS.between(dateTime, now);
        if (hours < 24) {
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        }
        
        long days = ChronoUnit.DAYS.between(dateTime, now);
        if (days < 7) {
            return days + (days == 1 ? " day ago" : " days ago");
        }
        
        long weeks = days / 7;
        if (weeks < 4) {
            return weeks + (weeks == 1 ? " week ago" : " weeks ago");
        }
        
        long months = ChronoUnit.MONTHS.between(dateTime, now);
        if (months < 12) {
            return months + (months == 1 ? " month ago" : " months ago");
        }
        
        long years = ChronoUnit.YEARS.between(dateTime, now);
        return years + (years == 1 ? " year ago" : " years ago");
    }
    
    /**
     * Get start of day for given date
     * @param dateTime the date
     * @return start of day timestamp
     */
    public static LocalDateTime getStartOfDay(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate().atStartOfDay() : null;
    }
    
    /**
     * Get end of day for given date
     * @param dateTime the date
     * @return end of day timestamp
     */
    public static LocalDateTime getEndOfDay(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate().atTime(23, 59, 59, 999999999) : null;
    }
    
    /**
     * Validate date format
     * @param dateString date string to validate
     * @param pattern expected pattern
     * @return true if valid format
     */
    public static boolean isValidDateFormat(String dateString, String pattern) {
        try {
            if (dateString == null || pattern == null) {
                return false;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime.parse(dateString, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get list of supported date patterns
     * @return list of supported patterns
     */
    public static List<String> getSupportedPatterns() {
        return Arrays.asList(
            DEFAULT_DATE_PATTERN,
            DATE_ONLY_PATTERN,
            TIME_ONLY_PATTERN,
            ISO_PATTERN,
            DISPLAY_PATTERN
        );
    }
    
    /**
     * Convert timestamp to ISO format string
     * @param dateTime the date time to convert
     * @return ISO formatted string
     */
    public static String toISOString(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(ISO_FORMATTER) : null;
    }
}
