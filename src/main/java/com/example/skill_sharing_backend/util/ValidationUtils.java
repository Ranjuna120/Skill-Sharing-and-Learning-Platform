package com.example.skill_sharing_backend.util;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Comprehensive validation utility class
 * Provides various validation methods for different data types and formats
 */
public class ValidationUtils {
    
    // Password strength patterns
    private static final Pattern WEAK_PASSWORD = Pattern.compile("^.{1,7}$");
    private static final Pattern MEDIUM_PASSWORD = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z]).{8,}$");
    private static final Pattern STRONG_PASSWORD = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
    private static final Pattern VERY_STRONG_PASSWORD = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$");
    
    // Credit card patterns
    private static final Pattern VISA_PATTERN = Pattern.compile("^4[0-9]{12}(?:[0-9]{3})?$");
    private static final Pattern MASTERCARD_PATTERN = Pattern.compile("^5[1-5][0-9]{14}$");
    private static final Pattern AMEX_PATTERN = Pattern.compile("^3[47][0-9]{13}$");
    
    // Other validation patterns
    private static final Pattern IP_ADDRESS_PATTERN = Pattern.compile(
        "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    );
    
    private static final Pattern MAC_ADDRESS_PATTERN = Pattern.compile(
        "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$"
    );
    
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile(
        "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"
    );
    
    /**
     * Password strength levels
     */
    public enum PasswordStrength {
        WEAK, MEDIUM, STRONG, VERY_STRONG
    }
    
    /**
     * Credit card types
     */
    public enum CreditCardType {
        VISA, MASTERCARD, AMEX, UNKNOWN
    }
    
    /**
     * Validation result class
     */
    public static class ValidationResult {
        private final boolean valid;
        private final List<String> errors;
        private final Map<String, Object> metadata;
        
        public ValidationResult(boolean valid) {
            this.valid = valid;
            this.errors = new ArrayList<>();
            this.metadata = new HashMap<>();
        }
        
        public ValidationResult(boolean valid, List<String> errors) {
            this.valid = valid;
            this.errors = errors != null ? new ArrayList<>(errors) : new ArrayList<>();
            this.metadata = new HashMap<>();
        }
        
        public boolean isValid() { return valid; }
        public List<String> getErrors() { return new ArrayList<>(errors); }
        public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
        
        public ValidationResult addError(String error) {
            this.errors.add(error);
            return this;
        }
        
        public ValidationResult addMetadata(String key, Object value) {
            this.metadata.put(key, value);
            return this;
        }
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private ValidationUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Validate required field
     * @param value field value
     * @param fieldName field name for error message
     * @return validation result
     */
    public static ValidationResult validateRequired(Object value, String fieldName) {
        if (value == null) {
            return new ValidationResult(false).addError(fieldName + " is required");
        }
        
        if (value instanceof String && StringUtils.isBlank((String) value)) {
            return new ValidationResult(false).addError(fieldName + " cannot be empty");
        }
        
        return new ValidationResult(true);
    }
    
    /**
     * Validate string length
     * @param value string value
     * @param fieldName field name
     * @param minLength minimum length
     * @param maxLength maximum length
     * @return validation result
     */
    public static ValidationResult validateLength(String value, String fieldName, int minLength, int maxLength) {
        List<String> errors = new ArrayList<>();
        
        if (value == null) {
            errors.add(fieldName + " is required");
            return new ValidationResult(false, errors);
        }
        
        if (value.length() < minLength) {
            errors.add(fieldName + " must be at least " + minLength + " characters long");
        }
        
        if (value.length() > maxLength) {
            errors.add(fieldName + " must not exceed " + maxLength + " characters");
        }
        
        ValidationResult result = new ValidationResult(errors.isEmpty(), errors);
        result.addMetadata("length", value.length());
        return result;
    }
    
    /**
     * Validate numeric range
     * @param value numeric value
     * @param fieldName field name
     * @param min minimum value
     * @param max maximum value
     * @return validation result
     */
    public static ValidationResult validateRange(Number value, String fieldName, Number min, Number max) {
        List<String> errors = new ArrayList<>();
        
        if (value == null) {
            errors.add(fieldName + " is required");
            return new ValidationResult(false, errors);
        }
        
        double doubleValue = value.doubleValue();
        
        if (min != null && doubleValue < min.doubleValue()) {
            errors.add(fieldName + " must be at least " + min);
        }
        
        if (max != null && doubleValue > max.doubleValue()) {
            errors.add(fieldName + " must not exceed " + max);
        }
        
        return new ValidationResult(errors.isEmpty(), errors);
    }
    
    /**
     * Validate email format
     * @param email email address
     * @return validation result
     */
    public static ValidationResult validateEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return new ValidationResult(false).addError("Email is required");
        }
        
        if (!StringUtils.isValidEmail(email)) {
            return new ValidationResult(false).addError("Invalid email format");
        }
        
        ValidationResult result = new ValidationResult(true);
        result.addMetadata("domain", email.substring(email.indexOf('@') + 1));
        return result;
    }
    
    /**
     * Validate phone number format
     * @param phone phone number
     * @return validation result
     */
    public static ValidationResult validatePhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return new ValidationResult(false).addError("Phone number is required");
        }
        
        if (!StringUtils.isValidPhone(phone)) {
            return new ValidationResult(false).addError("Invalid phone number format");
        }
        
        return new ValidationResult(true);
    }
    
    /**
     * Validate URL format
     * @param url URL string
     * @return validation result
     */
    public static ValidationResult validateUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return new ValidationResult(false).addError("URL is required");
        }
        
        if (!StringUtils.isValidUrl(url)) {
            return new ValidationResult(false).addError("Invalid URL format");
        }
        
        return new ValidationResult(true);
    }
    
    /**
     * Validate password strength
     * @param password password string
     * @return validation result with strength metadata
     */
    public static ValidationResult validatePassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return new ValidationResult(false).addError("Password is required");
        }
        
        PasswordStrength strength = getPasswordStrength(password);
        List<String> suggestions = getPasswordSuggestions(password);
        
        ValidationResult result = new ValidationResult(strength != PasswordStrength.WEAK);
        result.addMetadata("strength", strength);
        result.addMetadata("suggestions", suggestions);
        
        if (strength == PasswordStrength.WEAK) {
            result.addError("Password is too weak. Please use a stronger password.");
        }
        
        return result;
    }
    
    /**
     * Get password strength
     * @param password password string
     * @return password strength level
     */
    public static PasswordStrength getPasswordStrength(String password) {
        if (StringUtils.isEmpty(password)) {
            return PasswordStrength.WEAK;
        }
        
        if (VERY_STRONG_PASSWORD.matcher(password).matches()) {
            return PasswordStrength.VERY_STRONG;
        } else if (STRONG_PASSWORD.matcher(password).matches()) {
            return PasswordStrength.STRONG;
        } else if (MEDIUM_PASSWORD.matcher(password).matches()) {
            return PasswordStrength.MEDIUM;
        } else {
            return PasswordStrength.WEAK;
        }
    }
    
    /**
     * Get password improvement suggestions
     * @param password password string
     * @return list of suggestions
     */
    public static List<String> getPasswordSuggestions(String password) {
        List<String> suggestions = new ArrayList<>();
        
        if (StringUtils.isEmpty(password)) {
            suggestions.add("Password is required");
            return suggestions;
        }
        
        if (password.length() < 8) {
            suggestions.add("Use at least 8 characters");
        }
        
        if (!password.matches(".*[a-z].*")) {
            suggestions.add("Include lowercase letters");
        }
        
        if (!password.matches(".*[A-Z].*")) {
            suggestions.add("Include uppercase letters");
        }
        
        if (!password.matches(".*\\d.*")) {
            suggestions.add("Include numbers");
        }
        
        if (!password.matches(".*[@$!%*?&].*")) {
            suggestions.add("Include special characters (@$!%*?&)");
        }
        
        return suggestions;
    }
    
    /**
     * Validate credit card number
     * @param cardNumber credit card number
     * @return validation result with card type metadata
     */
    public static ValidationResult validateCreditCard(String cardNumber) {
        if (StringUtils.isEmpty(cardNumber)) {
            return new ValidationResult(false).addError("Credit card number is required");
        }
        
        // Remove spaces and hyphens
        String cleanNumber = cardNumber.replaceAll("[\\s-]", "");
        
        if (!StringUtils.isNumeric(cleanNumber)) {
            return new ValidationResult(false).addError("Credit card number must contain only digits");
        }
        
        CreditCardType cardType = getCreditCardType(cleanNumber);
        boolean isValid = isValidCreditCard(cleanNumber);
        
        ValidationResult result = new ValidationResult(isValid && cardType != CreditCardType.UNKNOWN);
        result.addMetadata("cardType", cardType);
        result.addMetadata("maskedNumber", StringUtils.maskString(cleanNumber, 4));
        
        if (!isValid) {
            result.addError("Invalid credit card number");
        }
        
        if (cardType == CreditCardType.UNKNOWN) {
            result.addError("Unsupported credit card type");
        }
        
        return result;
    }
    
    /**
     * Get credit card type
     * @param cardNumber clean card number
     * @return credit card type
     */
    public static CreditCardType getCreditCardType(String cardNumber) {
        if (VISA_PATTERN.matcher(cardNumber).matches()) {
            return CreditCardType.VISA;
        } else if (MASTERCARD_PATTERN.matcher(cardNumber).matches()) {
            return CreditCardType.MASTERCARD;
        } else if (AMEX_PATTERN.matcher(cardNumber).matches()) {
            return CreditCardType.AMEX;
        } else {
            return CreditCardType.UNKNOWN;
        }
    }
    
    /**
     * Validate credit card using Luhn algorithm
     * @param cardNumber clean card number
     * @return true if valid
     */
    private static boolean isValidCreditCard(String cardNumber) {
        if (StringUtils.isEmpty(cardNumber)) {
            return false;
        }
        
        int sum = 0;
        boolean alternate = false;
        
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            
            sum += digit;
            alternate = !alternate;
        }
        
        return sum % 10 == 0;
    }
    
    /**
     * Validate IP address format
     * @param ipAddress IP address string
     * @return validation result
     */
    public static ValidationResult validateIpAddress(String ipAddress) {
        if (StringUtils.isEmpty(ipAddress)) {
            return new ValidationResult(false).addError("IP address is required");
        }
        
        if (!IP_ADDRESS_PATTERN.matcher(ipAddress).matches()) {
            return new ValidationResult(false).addError("Invalid IP address format");
        }
        
        return new ValidationResult(true);
    }
    
    /**
     * Validate MAC address format
     * @param macAddress MAC address string
     * @return validation result
     */
    public static ValidationResult validateMacAddress(String macAddress) {
        if (StringUtils.isEmpty(macAddress)) {
            return new ValidationResult(false).addError("MAC address is required");
        }
        
        if (!MAC_ADDRESS_PATTERN.matcher(macAddress).matches()) {
            return new ValidationResult(false).addError("Invalid MAC address format");
        }
        
        return new ValidationResult(true);
    }
    
    /**
     * Validate hex color format
     * @param color hex color string
     * @return validation result
     */
    public static ValidationResult validateHexColor(String color) {
        if (StringUtils.isEmpty(color)) {
            return new ValidationResult(false).addError("Color is required");
        }
        
        if (!HEX_COLOR_PATTERN.matcher(color).matches()) {
            return new ValidationResult(false).addError("Invalid hex color format (use #RRGGBB or #RGB)");
        }
        
        return new ValidationResult(true);
    }
    
    /**
     * Validate age range
     * @param age age value
     * @return validation result
     */
    public static ValidationResult validateAge(Integer age) {
        if (age == null) {
            return new ValidationResult(false).addError("Age is required");
        }
        
        if (age < 0) {
            return new ValidationResult(false).addError("Age cannot be negative");
        }
        
        if (age > 150) {
            return new ValidationResult(false).addError("Age seems unrealistic");
        }
        
        ValidationResult result = new ValidationResult(true);
        
        // Add age category metadata
        if (age < 13) {
            result.addMetadata("category", "child");
        } else if (age < 20) {
            result.addMetadata("category", "teenager");
        } else if (age < 65) {
            result.addMetadata("category", "adult");
        } else {
            result.addMetadata("category", "senior");
        }
        
        return result;
    }
    
    /**
     * Validate username format
     * @param username username string
     * @return validation result
     */
    public static ValidationResult validateUsername(String username) {
        List<String> errors = new ArrayList<>();
        
        if (StringUtils.isEmpty(username)) {
            errors.add("Username is required");
            return new ValidationResult(false, errors);
        }
        
        if (username.length() < 3) {
            errors.add("Username must be at least 3 characters long");
        }
        
        if (username.length() > 30) {
            errors.add("Username must not exceed 30 characters");
        }
        
        if (!username.matches("^[a-zA-Z0-9._-]+$")) {
            errors.add("Username can only contain letters, numbers, dots, underscores, and hyphens");
        }
        
        if (username.startsWith(".") || username.startsWith("-") || username.startsWith("_")) {
            errors.add("Username cannot start with a special character");
        }
        
        if (username.endsWith(".") || username.endsWith("-") || username.endsWith("_")) {
            errors.add("Username cannot end with a special character");
        }
        
        return new ValidationResult(errors.isEmpty(), errors);
    }
    
    /**
     * Validate file extension
     * @param filename filename string
     * @param allowedExtensions allowed file extensions
     * @return validation result
     */
    public static ValidationResult validateFileExtension(String filename, List<String> allowedExtensions) {
        if (StringUtils.isEmpty(filename)) {
            return new ValidationResult(false).addError("Filename is required");
        }
        
        if (allowedExtensions == null || allowedExtensions.isEmpty()) {
            return new ValidationResult(true); // No restrictions
        }
        
        String extension = getFileExtension(filename);
        if (StringUtils.isEmpty(extension)) {
            return new ValidationResult(false).addError("File must have an extension");
        }
        
        boolean isAllowed = allowedExtensions.contains(extension.toLowerCase());
        ValidationResult result = new ValidationResult(isAllowed);
        result.addMetadata("extension", extension);
        result.addMetadata("allowedExtensions", allowedExtensions);
        
        if (!isAllowed) {
            result.addError("File type not allowed. Allowed types: " + String.join(", ", allowedExtensions));
        }
        
        return result;
    }
    
    /**
     * Extract file extension from filename
     * @param filename filename string
     * @return file extension
     */
    private static String getFileExtension(String filename) {
        if (StringUtils.isEmpty(filename)) {
            return "";
        }
        
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        
        return filename.substring(lastDotIndex + 1);
    }
}
