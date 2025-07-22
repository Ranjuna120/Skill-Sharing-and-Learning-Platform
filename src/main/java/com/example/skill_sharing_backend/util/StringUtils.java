package com.example.skill_sharing_backend.util;

import java.util.*;
import java.util.regex.Pattern;
import java.text.Normalizer;
import java.security.SecureRandom;

/**
 * Comprehensive String utility class
 * Provides various string manipulation, validation, and formatting methods
 */
public class StringUtils {
    
    // Common patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[1-9]?[0-9]{7,15}$"
    );
    
    private static final Pattern URL_PATTERN = Pattern.compile(
        "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
    );
    
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String NUMERIC = "0123456789";
    private static final String ALPHABETIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    
    private static final SecureRandom random = new SecureRandom();
    
    /**
     * Private constructor to prevent instantiation
     */
    private StringUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Check if string is null or empty
     * @param str string to check
     * @return true if null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Check if string is not null and not empty
     * @param str string to check
     * @return true if not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * Check if string is blank (null, empty or whitespace only)
     * @param str string to check
     * @return true if blank
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Check if string is not blank
     * @param str string to check
     * @return true if not blank
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
    
    /**
     * Capitalize first letter of string
     * @param str input string
     * @return capitalized string
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    /**
     * Convert string to camelCase
     * @param str input string
     * @return camelCase string
     */
    public static String toCamelCase(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        String[] words = str.toLowerCase().split("[\\s_-]+");
        StringBuilder result = new StringBuilder(words[0]);
        
        for (int i = 1; i < words.length; i++) {
            result.append(capitalize(words[i]));
        }
        
        return result.toString();
    }
    
    /**
     * Convert string to PascalCase
     * @param str input string
     * @return PascalCase string
     */
    public static String toPascalCase(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        String camelCase = toCamelCase(str);
        return capitalize(camelCase);
    }
    
    /**
     * Convert string to snake_case
     * @param str input string
     * @return snake_case string
     */
    public static String toSnakeCase(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        return str.replaceAll("([a-z])([A-Z])", "$1_$2")
                  .replaceAll("[\\s-]+", "_")
                  .toLowerCase();
    }
    
    /**
     * Convert string to kebab-case
     * @param str input string
     * @return kebab-case string
     */
    public static String toKebabCase(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        return str.replaceAll("([a-z])([A-Z])", "$1-$2")
                  .replaceAll("[\\s_]+", "-")
                  .toLowerCase();
    }
    
    /**
     * Truncate string to specified length with ellipsis
     * @param str input string
     * @param maxLength maximum length
     * @return truncated string
     */
    public static String truncate(String str, int maxLength) {
        if (isEmpty(str) || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * Truncate string to specified length with custom suffix
     * @param str input string
     * @param maxLength maximum length
     * @param suffix suffix to add
     * @return truncated string
     */
    public static String truncate(String str, int maxLength, String suffix) {
        if (isEmpty(str) || str.length() <= maxLength) {
            return str;
        }
        suffix = suffix == null ? "" : suffix;
        return str.substring(0, maxLength - suffix.length()) + suffix;
    }
    
    /**
     * Reverse a string
     * @param str input string
     * @return reversed string
     */
    public static String reverse(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return new StringBuilder(str).reverse().toString();
    }
    
    /**
     * Count occurrences of substring in string
     * @param str main string
     * @param substring substring to count
     * @return number of occurrences
     */
    public static int countOccurrences(String str, String substring) {
        if (isEmpty(str) || isEmpty(substring)) {
            return 0;
        }
        
        int count = 0;
        int index = 0;
        
        while ((index = str.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        
        return count;
    }
    
    /**
     * Remove all whitespace from string
     * @param str input string
     * @return string without whitespace
     */
    public static String removeWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("\\s+", "");
    }
    
    /**
     * Remove extra whitespace (multiple spaces become single space)
     * @param str input string
     * @return normalized string
     */
    public static String normalizeWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("\\s+", " ").trim();
    }
    
    /**
     * Check if string contains only digits
     * @param str string to check
     * @return true if numeric
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("\\d+");
    }
    
    /**
     * Check if string contains only alphabetic characters
     * @param str string to check
     * @return true if alphabetic
     */
    public static boolean isAlphabetic(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("[a-zA-Z]+");
    }
    
    /**
     * Check if string contains only alphanumeric characters
     * @param str string to check
     * @return true if alphanumeric
     */
    public static boolean isAlphanumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("[a-zA-Z0-9]+");
    }
    
    /**
     * Validate email format
     * @param email email to validate
     * @return true if valid email
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validate phone number format
     * @param phone phone number to validate
     * @return true if valid phone
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.replaceAll("\\s+", "")).matches();
    }
    
    /**
     * Validate URL format
     * @param url URL to validate
     * @return true if valid URL
     */
    public static boolean isValidUrl(String url) {
        if (isEmpty(url)) {
            return false;
        }
        return URL_PATTERN.matcher(url).matches();
    }
    
    /**
     * Generate random string of specified length
     * @param length desired length
     * @return random string
     */
    public static String generateRandomString(int length) {
        return generateRandomString(length, ALPHANUMERIC);
    }
    
    /**
     * Generate random string with custom character set
     * @param length desired length
     * @param charset character set to use
     * @return random string
     */
    public static String generateRandomString(int length, String charset) {
        if (length <= 0 || isEmpty(charset)) {
            return "";
        }
        
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(charset.charAt(random.nextInt(charset.length())));
        }
        return result.toString();
    }
    
    /**
     * Generate random numeric string
     * @param length desired length
     * @return random numeric string
     */
    public static String generateRandomNumeric(int length) {
        return generateRandomString(length, NUMERIC);
    }
    
    /**
     * Generate random alphabetic string
     * @param length desired length
     * @return random alphabetic string
     */
    public static String generateRandomAlphabetic(int length) {
        return generateRandomString(length, ALPHABETIC);
    }
    
    /**
     * Mask string (show only first and last few characters)
     * @param str input string
     * @param visibleChars number of visible characters at start and end
     * @return masked string
     */
    public static String maskString(String str, int visibleChars) {
        if (isEmpty(str) || str.length() <= visibleChars * 2) {
            return str;
        }
        
        String start = str.substring(0, visibleChars);
        String end = str.substring(str.length() - visibleChars);
        String middle = "*".repeat(str.length() - visibleChars * 2);
        
        return start + middle + end;
    }
    
    /**
     * Remove accents and diacritics from string
     * @param str input string
     * @return normalized string
     */
    public static String removeAccents(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }
    
    /**
     * Convert string to URL-friendly slug
     * @param str input string
     * @return slug string
     */
    public static String toSlug(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        return removeAccents(str)
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }
    
    /**
     * Split string into words
     * @param str input string
     * @return list of words
     */
    public static List<String> getWords(String str) {
        if (isEmpty(str)) {
            return new ArrayList<>();
        }
        
        return Arrays.asList(str.trim().split("\\s+"));
    }
    
    /**
     * Get word count
     * @param str input string
     * @return number of words
     */
    public static int getWordCount(String str) {
        return getWords(str).size();
    }
    
    /**
     * Join collection of strings with delimiter
     * @param collection collection of strings
     * @param delimiter delimiter to use
     * @return joined string
     */
    public static String join(Collection<String> collection, String delimiter) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        return String.join(delimiter == null ? "" : delimiter, collection);
    }
    
    /**
     * Join array of strings with delimiter
     * @param array array of strings
     * @param delimiter delimiter to use
     * @return joined string
     */
    public static String join(String[] array, String delimiter) {
        if (array == null || array.length == 0) {
            return "";
        }
        return String.join(delimiter == null ? "" : delimiter, array);
    }
    
    /**
     * Pad string to left with specified character
     * @param str input string
     * @param length desired length
     * @param padChar padding character
     * @return padded string
     */
    public static String padLeft(String str, int length, char padChar) {
        if (str == null) {
            str = "";
        }
        
        if (str.length() >= length) {
            return str;
        }
        
        StringBuilder padded = new StringBuilder();
        for (int i = 0; i < length - str.length(); i++) {
            padded.append(padChar);
        }
        padded.append(str);
        
        return padded.toString();
    }
    
    /**
     * Pad string to right with specified character
     * @param str input string
     * @param length desired length
     * @param padChar padding character
     * @return padded string
     */
    public static String padRight(String str, int length, char padChar) {
        if (str == null) {
            str = "";
        }
        
        if (str.length() >= length) {
            return str;
        }
        
        StringBuilder padded = new StringBuilder(str);
        for (int i = 0; i < length - str.length(); i++) {
            padded.append(padChar);
        }
        
        return padded.toString();
    }
    
    /**
     * Center string with padding
     * @param str input string
     * @param length desired length
     * @param padChar padding character
     * @return centered string
     */
    public static String center(String str, int length, char padChar) {
        if (str == null) {
            str = "";
        }
        
        if (str.length() >= length) {
            return str;
        }
        
        int totalPadding = length - str.length();
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;
        
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < leftPadding; i++) {
            result.append(padChar);
        }
        result.append(str);
        for (int i = 0; i < rightPadding; i++) {
            result.append(padChar);
        }
        
        return result.toString();
    }
    
    /**
     * Extract initials from a name
     * @param name full name
     * @return initials
     */
    public static String getInitials(String name) {
        if (isEmpty(name)) {
            return "";
        }
        
        StringBuilder initials = new StringBuilder();
        String[] words = name.trim().split("\\s+");
        
        for (String word : words) {
            if (isNotEmpty(word)) {
                initials.append(word.charAt(0));
            }
        }
        
        return initials.toString().toUpperCase();
    }
}
