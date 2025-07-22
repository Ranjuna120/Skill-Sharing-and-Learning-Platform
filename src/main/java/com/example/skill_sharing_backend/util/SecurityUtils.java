package com.example.skill_sharing_backend.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 * Comprehensive security utility class
 * Provides encryption, hashing, and security validation methods
 */
public class SecurityUtils {
    
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    
    // Password strength patterns
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile(".*[a-z].*");
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile(".*[A-Z].*");
    private static final Pattern DIGIT_PATTERN = Pattern.compile(".*[0-9].*");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    
    // Common weak passwords
    private static final String[] COMMON_PASSWORDS = {
        "password", "123456", "123456789", "qwerty", "abc123", "password123",
        "admin", "letmein", "welcome", "monkey", "dragon", "password1"
    };
    
    /**
     * Private constructor to prevent instantiation
     */
    private SecurityUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Generate a secure random password
     * @param length password length
     * @param includeSpecialChars whether to include special characters
     * @return generated password
     */
    public static String generatePassword(int length, boolean includeSpecialChars) {
        if (length < 4) {
            throw new IllegalArgumentException("Password length must be at least 4 characters");
        }
        
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        
        StringBuilder charset = new StringBuilder();
        charset.append(lowercase).append(uppercase).append(digits);
        
        if (includeSpecialChars) {
            charset.append(specialChars);
        }
        
        StringBuilder password = new StringBuilder();
        
        // Ensure at least one character from each required category
        password.append(lowercase.charAt(SECURE_RANDOM.nextInt(lowercase.length())));
        password.append(uppercase.charAt(SECURE_RANDOM.nextInt(uppercase.length())));
        password.append(digits.charAt(SECURE_RANDOM.nextInt(digits.length())));
        
        if (includeSpecialChars) {
            password.append(specialChars.charAt(SECURE_RANDOM.nextInt(specialChars.length())));
        }
        
        // Fill remaining length with random characters
        int remainingLength = length - password.length();
        for (int i = 0; i < remainingLength; i++) {
            password.append(charset.charAt(SECURE_RANDOM.nextInt(charset.length())));
        }
        
        // Shuffle the password characters
        return shuffleString(password.toString());
    }
    
    /**
     * Shuffle characters in a string
     * @param input input string
     * @return shuffled string
     */
    private static String shuffleString(String input) {
        char[] chars = input.toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = SECURE_RANDOM.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        return new String(chars);
    }
    
    /**
     * Validate password strength
     * @param password password to validate
     * @return password strength assessment
     */
    public static PasswordStrength validatePasswordStrength(String password) {
        if (StringUtils.isBlank(password)) {
            return new PasswordStrength(StrengthLevel.VERY_WEAK, "Password cannot be empty", 0);
        }
        
        int score = 0;
        StringBuilder feedback = new StringBuilder();
        
        // Length check
        if (password.length() < 8) {
            feedback.append("Password should be at least 8 characters long. ");
        } else if (password.length() >= 12) {
            score += 2;
        } else {
            score += 1;
        }
        
        // Character variety checks
        if (LOWERCASE_PATTERN.matcher(password).matches()) {
            score += 1;
        } else {
            feedback.append("Add lowercase letters. ");
        }
        
        if (UPPERCASE_PATTERN.matcher(password).matches()) {
            score += 1;
        } else {
            feedback.append("Add uppercase letters. ");
        }
        
        if (DIGIT_PATTERN.matcher(password).matches()) {
            score += 1;
        } else {
            feedback.append("Add numbers. ");
        }
        
        if (SPECIAL_CHAR_PATTERN.matcher(password).matches()) {
            score += 2;
        } else {
            feedback.append("Add special characters. ");
        }
        
        // Common password check
        String lowerPassword = password.toLowerCase();
        for (String commonPassword : COMMON_PASSWORDS) {
            if (lowerPassword.contains(commonPassword)) {
                score -= 2;
                feedback.append("Avoid common passwords. ");
                break;
            }
        }
        
        // Sequential characters check
        if (hasSequentialCharacters(password)) {
            score -= 1;
            feedback.append("Avoid sequential characters. ");
        }
        
        // Repeated characters check
        if (hasRepeatedCharacters(password)) {
            score -= 1;
            feedback.append("Avoid repeated characters. ");
        }
        
        // Determine strength level
        StrengthLevel level;
        if (score < 2) {
            level = StrengthLevel.VERY_WEAK;
        } else if (score < 4) {
            level = StrengthLevel.WEAK;
        } else if (score < 6) {
            level = StrengthLevel.MEDIUM;
        } else if (score < 8) {
            level = StrengthLevel.STRONG;
        } else {
            level = StrengthLevel.VERY_STRONG;
        }
        
        String feedbackMessage = feedback.length() > 0 ? 
            feedback.toString().trim() : "Password strength is good";
        
        return new PasswordStrength(level, feedbackMessage, Math.max(0, score));
    }
    
    /**
     * Check for sequential characters in password
     * @param password password to check
     * @return true if has sequential characters
     */
    private static boolean hasSequentialCharacters(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            char c1 = password.charAt(i);
            char c2 = password.charAt(i + 1);
            char c3 = password.charAt(i + 2);
            
            if (c2 == c1 + 1 && c3 == c2 + 1) {
                return true;
            }
            if (c2 == c1 - 1 && c3 == c2 - 1) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check for repeated characters in password
     * @param password password to check
     * @return true if has repeated characters
     */
    private static boolean hasRepeatedCharacters(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            if (password.charAt(i) == password.charAt(i + 1) && 
                password.charAt(i + 1) == password.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Generate secure random salt
     * @param length salt length in bytes
     * @return base64 encoded salt
     */
    public static String generateSalt(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Salt length must be positive");
        }
        
        byte[] salt = new byte[length];
        SECURE_RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * Hash password with salt using SHA-256
     * @param password password to hash
     * @param salt salt value
     * @return hashed password
     */
    public static String hashPassword(String password, String salt) {
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        if (StringUtils.isBlank(salt)) {
            throw new IllegalArgumentException("Salt cannot be empty");
        }
        
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    /**
     * Verify password against hash
     * @param password plain text password
     * @param salt salt used for hashing
     * @param hash expected hash
     * @return true if password matches hash
     */
    public static boolean verifyPassword(String password, String salt, String hash) {
        if (StringUtils.isBlank(hash)) {
            return false;
        }
        
        try {
            String computedHash = hashPassword(password, salt);
            return hash.equals(computedHash);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Generate AES encryption key
     * @return base64 encoded key
     */
    public static String generateAESKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
            keyGenerator.init(256);
            SecretKey secretKey = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("AES algorithm not available", e);
        }
    }
    
    /**
     * Encrypt text using AES-GCM
     * @param plainText text to encrypt
     * @param keyBase64 base64 encoded AES key
     * @return encrypted data (IV + encrypted text + tag) as base64
     */
    public static String encryptAES(String plainText, String keyBase64) {
        if (StringUtils.isBlank(plainText)) {
            throw new IllegalArgumentException("Plain text cannot be empty");
        }
        
        if (StringUtils.isBlank(keyBase64)) {
            throw new IllegalArgumentException("Encryption key cannot be empty");
        }
        
        try {
            byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, AES_ALGORITHM);
            
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            
            // Generate random IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            SECURE_RANDOM.nextBytes(iv);
            
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            
            byte[] encryptedData = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            
            // Combine IV and encrypted data
            byte[] encryptedWithIv = new byte[GCM_IV_LENGTH + encryptedData.length];
            System.arraycopy(iv, 0, encryptedWithIv, 0, GCM_IV_LENGTH);
            System.arraycopy(encryptedData, 0, encryptedWithIv, GCM_IV_LENGTH, encryptedData.length);
            
            return Base64.getEncoder().encodeToString(encryptedWithIv);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
    
    /**
     * Decrypt text using AES-GCM
     * @param encryptedDataBase64 encrypted data as base64
     * @param keyBase64 base64 encoded AES key
     * @return decrypted plain text
     */
    public static String decryptAES(String encryptedDataBase64, String keyBase64) {
        if (StringUtils.isBlank(encryptedDataBase64)) {
            throw new IllegalArgumentException("Encrypted data cannot be empty");
        }
        
        if (StringUtils.isBlank(keyBase64)) {
            throw new IllegalArgumentException("Decryption key cannot be empty");
        }
        
        try {
            byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, AES_ALGORITHM);
            
            byte[] encryptedWithIv = Base64.getDecoder().decode(encryptedDataBase64);
            
            if (encryptedWithIv.length < GCM_IV_LENGTH + GCM_TAG_LENGTH) {
                throw new IllegalArgumentException("Invalid encrypted data length");
            }
            
            // Extract IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            System.arraycopy(encryptedWithIv, 0, iv, 0, GCM_IV_LENGTH);
            
            // Extract encrypted data
            byte[] encryptedData = new byte[encryptedWithIv.length - GCM_IV_LENGTH];
            System.arraycopy(encryptedWithIv, GCM_IV_LENGTH, encryptedData, 0, encryptedData.length);
            
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            
            byte[] decryptedData = cipher.doFinal(encryptedData);
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
    
    /**
     * Generate secure random token
     * @param length token length in bytes
     * @return base64 encoded token
     */
    public static String generateSecureToken(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Token length must be positive");
        }
        
        byte[] token = new byte[length];
        SECURE_RANDOM.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }
    
    /**
     * Calculate SHA-256 hash of input
     * @param input input string
     * @return hash as hex string
     */
    public static String sha256Hash(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    /**
     * Validate email format
     * @param email email to validate
     * @return true if valid email format
     */
    public static boolean isValidEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }
    
    /**
     * Sanitize input string to prevent XSS attacks
     * @param input input string
     * @return sanitized string
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        
        return input.replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;")
                   .replace("&", "&amp;");
    }
    
    /**
     * Validate URL format and safety
     * @param url URL to validate
     * @return true if valid and safe URL
     */
    public static boolean isValidAndSafeUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return false;
        }
        
        // Basic URL pattern
        String urlPattern = "^https?://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(/.*)?$";
        if (!Pattern.matches(urlPattern, url)) {
            return false;
        }
        
        // Check for dangerous protocols
        String lowerUrl = url.toLowerCase();
        String[] dangerousProtocols = {"javascript:", "data:", "file:", "ftp:"};
        for (String protocol : dangerousProtocols) {
            if (lowerUrl.startsWith(protocol)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Password strength enumeration
     */
    public enum StrengthLevel {
        VERY_WEAK(0, "Very Weak"),
        WEAK(1, "Weak"),
        MEDIUM(2, "Medium"),
        STRONG(3, "Strong"),
        VERY_STRONG(4, "Very Strong");
        
        private final int level;
        private final String description;
        
        StrengthLevel(int level, String description) {
            this.level = level;
            this.description = description;
        }
        
        public int getLevel() { return level; }
        public String getDescription() { return description; }
    }
    
    /**
     * Password strength assessment result
     */
    public static class PasswordStrength {
        private final StrengthLevel level;
        private final String feedback;
        private final int score;
        
        public PasswordStrength(StrengthLevel level, String feedback, int score) {
            this.level = level;
            this.feedback = feedback;
            this.score = score;
        }
        
        public StrengthLevel getLevel() { return level; }
        public String getFeedback() { return feedback; }
        public int getScore() { return score; }
        
        @Override
        public String toString() {
            return String.format("PasswordStrength{level=%s, score=%d, feedback='%s'}",
                    level, score, feedback);
        }
    }
}
