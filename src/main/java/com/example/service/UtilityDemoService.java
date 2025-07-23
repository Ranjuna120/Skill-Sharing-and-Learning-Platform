package com.example.service;

import org.springframework.stereotype.Service;
import com.example.skill_sharing_backend.util.*;
import com.example.skill_sharing_backend.service.DataProcessingService;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Example service demonstrating how to use utility classes
 * Shows practical integration of all utility classes in business logic
 */
@Service
public class UtilityDemoService {
    
    private final DataProcessingService dataProcessingService;
    
    public UtilityDemoService(DataProcessingService dataProcessingService) {
        this.dataProcessingService = dataProcessingService;
    }
    
    /**
     * Process user registration with comprehensive validation
     * Demonstrates ValidationUtils, StringUtils, and SecurityUtils usage
     */
    public RegistrationResult processUserRegistration(String email, String username, String password, String fullName) {
        List<String> errors = new ArrayList<>();
        
        // 1. Use StringUtils for cleaning input
        email = StringUtils.normalizeWhitespace(email);
        username = StringUtils.normalizeWhitespace(username);
        fullName = StringUtils.normalizeWhitespace(fullName);
        
        // 2. Use ValidationUtils for validation
        ValidationUtils.ValidationResult emailValidation = ValidationUtils.validateEmail(email);
        if (!emailValidation.isValid()) {
            errors.addAll(emailValidation.getErrors());
        }
        
        ValidationUtils.ValidationResult usernameValidation = ValidationUtils.validateUsername(username);
        if (!usernameValidation.isValid()) {
            errors.addAll(usernameValidation.getErrors());
        }
        
        ValidationUtils.ValidationResult passwordValidation = ValidationUtils.validatePassword(password);
        if (!passwordValidation.isValid()) {
            errors.addAll(passwordValidation.getErrors());
        }
        
        // 3. Use SecurityUtils for password strength check
        SecurityUtils.PasswordStrength passwordStrength = SecurityUtils.validatePasswordStrength(password);
        if (passwordStrength.getLevel().getLevel() < 2) { // Require at least MEDIUM strength
            errors.add("Password too weak: " + passwordStrength.getFeedback());
        }
        
        // 4. Use StringUtils for name formatting
        String formattedName = StringUtils.capitalize(fullName);
        
        // 5. If validation passed, create secure password hash
        String passwordHash = null;
        String salt = null;
        if (errors.isEmpty()) {
            salt = SecurityUtils.generateSalt(32);
            passwordHash = SecurityUtils.hashPassword(password, salt);
        }
        
        return new RegistrationResult(
                errors.isEmpty(),
                errors,
                formattedName,
                passwordHash,
                salt,
                passwordStrength.getLevel().getDescription()
        );
    }
    
    /**
     * Analyze user activity data using mathematical and data processing utilities
     * Demonstrates MathUtils, CollectionUtils, and DataProcessingService usage
     */
    public UserActivityAnalysis analyzeUserActivity(List<ActivityLog> activityLogs) {
        if (CollectionUtils.isEmpty(activityLogs)) {
            return new UserActivityAnalysis();
        }
        
        // 1. Use CollectionUtils to filter and process data
        List<ActivityLog> recentLogs = CollectionUtils.filter(activityLogs, 
            log -> DateUtils.isToday(log.getTimestamp()) || DateUtils.isPast(log.getTimestamp()));
        
        // 2. Extract activity scores for mathematical analysis
        List<Integer> activityScores = CollectionUtils.map(recentLogs, ActivityLog::getScore);
        
        // 3. Use DataProcessingService for advanced statistics
        DataProcessingService.DataStatistics stats = dataProcessingService.calculateStatistics(activityScores);
        
        // 4. Use MathUtils for additional calculations
        double averageScore = MathUtils.average(activityScores);
        
        // 5. Calculate activity trend
        List<Double> scoreDoubles = CollectionUtils.map(activityScores, score -> score.doubleValue());
        DataProcessingService.TrendAnalysis trend = dataProcessingService.analyzeTrend(scoreDoubles);
        
        // 6. Find most common activity types
        List<String> activityTypes = CollectionUtils.map(recentLogs, ActivityLog::getType);
        DataProcessingService.PatternAnalysis<String> patterns = dataProcessingService.analyzePatterns(activityTypes);
        
        // 7. Calculate activity consistency using standard deviation
        double[] scoresArray = activityScores.stream().mapToDouble(Integer::doubleValue).toArray();
        double consistency = 100 - MathUtils.standardDeviation(scoresArray); // Higher = more consistent
        
        return new UserActivityAnalysis(
                recentLogs.size(),
                averageScore,
                stats.getMax(),
                stats.getMin(),
                consistency,
                trend.getDirection().toString(),
                patterns.getMostFrequent(),
                DateUtils.formatDateTime(DateUtils.getCurrentTimestamp())
        );
    }
    
    /**
     * Generate secure user tokens using security utilities
     * Demonstrates SecurityUtils and DateUtils integration
     */
    public UserToken generateUserToken(String userId, int validityHours) {
        // 1. Generate secure token
        String token = SecurityUtils.generateSecureToken(32);
        
        // 2. Calculate expiry time using DateUtils
        LocalDateTime now = DateUtils.getCurrentTimestamp();
        LocalDateTime expiryTime = DateUtils.addHours(now, validityHours);
        
        // 3. Create token hash for database storage
        String tokenHash = SecurityUtils.sha256Hash(token);
        
        return new UserToken(
                token,
                tokenHash,
                userId,
                DateUtils.formatDateTime(now),
                DateUtils.formatDateTime(expiryTime)
        );
    }
    
    /**
     * Process and clean user-generated content
     * Demonstrates StringUtils and ValidationUtils for content processing
     */
    public ContentProcessingResult processUserContent(String content, int maxLength) {
        List<String> warnings = new ArrayList<>();
        
        // 1. Basic cleanup
        String cleaned = StringUtils.normalizeWhitespace(content);
        
        // 2. Length validation
        ValidationUtils.ValidationResult lengthCheck = 
            ValidationUtils.validateLength(cleaned, "content", 1, maxLength);
        
        if (!lengthCheck.isValid()) {
            return new ContentProcessingResult(false, null, lengthCheck.getErrors());
        }
        
        // 3. Additional processing
        int wordCount = StringUtils.getWordCount(cleaned);
        if (wordCount > maxLength / 5) { // Average 5 chars per word
            warnings.add("Content may be too long for optimal readability");
        }
        
        // 4. Check for potentially problematic content
        String lowerContent = cleaned.toLowerCase();
        if (lowerContent.contains("spam") || lowerContent.contains("advertisement")) {
            warnings.add("Content may contain promotional material");
        }
        
        // 5. Generate content summary
        String summary = StringUtils.truncate(cleaned, 100) + 
                        String.format(" [%d words, %d characters]", wordCount, cleaned.length());
        
        return new ContentProcessingResult(true, cleaned, warnings, summary);
    }
    
    /**
     * Calculate file processing metrics
     * Demonstrates FileUtils and MathUtils integration
     */
    public FileProcessingMetrics calculateFileMetrics(List<String> filePaths) {
        List<Long> fileSizes = new ArrayList<>();
        List<String> extensions = new ArrayList<>();
        int totalFiles = 0;
        int accessibleFiles = 0;
        
        for (String filePath : filePaths) {
            totalFiles++;
            
            if (FileUtils.exists(filePath)) {
                accessibleFiles++;
                
                try {
                    long size = FileUtils.getFileSize(filePath);
                    fileSizes.add(size);
                    
                    String extension = FileUtils.getFileExtension(filePath);
                    if (StringUtils.isNotBlank(extension)) {
                        extensions.add(extension);
                    }
                } catch (Exception e) {
                    // File exists but can't read size
                }
            }
        }
        
        // Calculate statistics
        double totalSizeBytes = MathUtils.sum(fileSizes);
        double averageSizeBytes = fileSizes.isEmpty() ? 0 : MathUtils.average(fileSizes);
        
        // Find most common extension
        DataProcessingService.PatternAnalysis<String> extensionPattern = 
            dataProcessingService.analyzePatterns(extensions);
        
        return new FileProcessingMetrics(
                totalFiles,
                accessibleFiles,
                FileUtils.getHumanReadableFileSize((long) totalSizeBytes),
                FileUtils.getHumanReadableFileSize((long) averageSizeBytes),
                extensionPattern.getMostFrequent(),
                extensionPattern.getUniqueCount()
        );
    }
    
    // Data classes for return types
    
    public static class RegistrationResult {
        private final boolean success;
        private final List<String> errors;
        private final String formattedName;
        private final String passwordHash;
        private final String salt;
        private final String passwordStrength;
        
        public RegistrationResult(boolean success, List<String> errors, String formattedName, 
                                String passwordHash, String salt, String passwordStrength) {
            this.success = success;
            this.errors = errors;
            this.formattedName = formattedName;
            this.passwordHash = passwordHash;
            this.salt = salt;
            this.passwordStrength = passwordStrength;
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public List<String> getErrors() { return errors; }
        public String getFormattedName() { return formattedName; }
        public String getPasswordHash() { return passwordHash; }
        public String getSalt() { return salt; }
        public String getPasswordStrength() { return passwordStrength; }
    }
    
    public static class UserActivityAnalysis {
        private final int totalActivities;
        private final double averageScore;
        private final double maxScore;
        private final double minScore;
        private final double consistency;
        private final String trend;
        private final String mostCommonActivity;
        private final String analysisTime;
        
        public UserActivityAnalysis() {
            this(0, 0, 0, 0, 0, "FLAT", "None", "");
        }
        
        public UserActivityAnalysis(int totalActivities, double averageScore, double maxScore, 
                                  double minScore, double consistency, String trend, 
                                  String mostCommonActivity, String analysisTime) {
            this.totalActivities = totalActivities;
            this.averageScore = averageScore;
            this.maxScore = maxScore;
            this.minScore = minScore;
            this.consistency = consistency;
            this.trend = trend;
            this.mostCommonActivity = mostCommonActivity;
            this.analysisTime = analysisTime;
        }
        
        // Getters
        public int getTotalActivities() { return totalActivities; }
        public double getAverageScore() { return averageScore; }
        public double getMaxScore() { return maxScore; }
        public double getMinScore() { return minScore; }
        public double getConsistency() { return consistency; }
        public String getTrend() { return trend; }
        public String getMostCommonActivity() { return mostCommonActivity; }
        public String getAnalysisTime() { return analysisTime; }
    }
    
    public static class UserToken {
        private final String token;
        private final String tokenHash;
        private final String userId;
        private final String createdAt;
        private final String expiresAt;
        
        public UserToken(String token, String tokenHash, String userId, String createdAt, String expiresAt) {
            this.token = token;
            this.tokenHash = tokenHash;
            this.userId = userId;
            this.createdAt = createdAt;
            this.expiresAt = expiresAt;
        }
        
        // Getters
        public String getToken() { return token; }
        public String getTokenHash() { return tokenHash; }
        public String getUserId() { return userId; }
        public String getCreatedAt() { return createdAt; }
        public String getExpiresAt() { return expiresAt; }
    }
    
    public static class ContentProcessingResult {
        private final boolean success;
        private final String processedContent;
        private final List<String> warnings;
        private final String summary;
        
        public ContentProcessingResult(boolean success, String processedContent, List<String> warnings) {
            this(success, processedContent, warnings, null);
        }
        
        public ContentProcessingResult(boolean success, String processedContent, List<String> warnings, String summary) {
            this.success = success;
            this.processedContent = processedContent;
            this.warnings = warnings;
            this.summary = summary;
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getProcessedContent() { return processedContent; }
        public List<String> getWarnings() { return warnings; }
        public String getSummary() { return summary; }
    }
    
    public static class FileProcessingMetrics {
        private final int totalFiles;
        private final int accessibleFiles;
        private final String totalSize;
        private final String averageSize;
        private final String mostCommonExtension;
        private final int uniqueExtensions;
        
        public FileProcessingMetrics(int totalFiles, int accessibleFiles, String totalSize, 
                                   String averageSize, String mostCommonExtension, int uniqueExtensions) {
            this.totalFiles = totalFiles;
            this.accessibleFiles = accessibleFiles;
            this.totalSize = totalSize;
            this.averageSize = averageSize;
            this.mostCommonExtension = mostCommonExtension;
            this.uniqueExtensions = uniqueExtensions;
        }
        
        // Getters
        public int getTotalFiles() { return totalFiles; }
        public int getAccessibleFiles() { return accessibleFiles; }
        public String getTotalSize() { return totalSize; }
        public String getAverageSize() { return averageSize; }
        public String getMostCommonExtension() { return mostCommonExtension; }
        public int getUniqueExtensions() { return uniqueExtensions; }
    }
    
    // Helper class for activity log
    public static class ActivityLog {
        private final LocalDateTime timestamp;
        private final String type;
        private final int score;
        
        public ActivityLog(LocalDateTime timestamp, String type, int score) {
            this.timestamp = timestamp;
            this.type = type;
            this.score = score;
        }
        
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getType() { return type; }
        public int getScore() { return score; }
    }
}
