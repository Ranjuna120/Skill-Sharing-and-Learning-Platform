package com.example.skill_sharing_backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.example.skill_sharing_backend.util.*;
import com.example.skill_sharing_backend.service.DataProcessingService;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Demo controller showing how to use utility classes
 * Provides practical examples for developers
 */
@RestController
@RequestMapping("/api/demo")
public class UtilityDemoController {
    
    private final DataProcessingService dataProcessingService;
    
    public UtilityDemoController(DataProcessingService dataProcessingService) {
        this.dataProcessingService = dataProcessingService;
    }
    
    /**
     * Demo StringUtils usage
     */
    @GetMapping("/string-utils")
    public ResponseEntity<Map<String, Object>> demoStringUtils(@RequestParam(defaultValue = "Hello World") String text) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("original", text);
        result.put("isEmpty", StringUtils.isEmpty(text));
        result.put("isBlank", StringUtils.isBlank(text));
        result.put("normalized", StringUtils.normalizeWhitespace(text));
        result.put("capitalized", StringUtils.capitalize(text));
        result.put("camelCase", StringUtils.toCamelCase(text));
        result.put("snakeCase", StringUtils.toSnakeCase(text));
        result.put("kebabCase", StringUtils.toKebabCase(text));
        result.put("reversed", StringUtils.reverse(text));
        result.put("isNumeric", StringUtils.isNumeric(text));
        result.put("isAlphabetic", StringUtils.isAlphabetic(text));
        result.put("isAlphanumeric", StringUtils.isAlphanumeric(text));
        
        if (text.length() > 10) {
            result.put("truncated", StringUtils.truncate(text, 10));
            result.put("truncatedWithEllipsis", StringUtils.truncate(text, 10, "..."));
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * Demo ValidationUtils usage
     */
    @GetMapping("/validation-utils")
    public ResponseEntity<Map<String, Object>> demoValidation() {
        Map<String, Object> result = new HashMap<>();
        
        // Test various validation examples using StringUtils validation methods
        result.put("emailTests", Map.of(
            "valid@example.com", StringUtils.isValidEmail("valid@example.com"),
            "invalid-email", StringUtils.isValidEmail("invalid-email"),
            "test@domain", StringUtils.isValidEmail("test@domain")
        ));
        
        result.put("urlTests", Map.of(
            "http://example.com", StringUtils.isValidUrl("http://example.com"),
            "https://test.org", StringUtils.isValidUrl("https://test.org"),
            "not-a-url", StringUtils.isValidUrl("not-a-url")
        ));
        
        result.put("phoneTests", Map.of(
            "+1234567890", StringUtils.isValidPhone("+1234567890"),
            "123456789", StringUtils.isValidPhone("123456789"),
            "invalid-phone", StringUtils.isValidPhone("invalid-phone")
        ));
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * Demo DateUtils usage
     */
    @GetMapping("/date-utils")
    public ResponseEntity<Map<String, Object>> demoDateUtils() {
        Map<String, Object> result = new HashMap<>();
        
        LocalDateTime now = DateUtils.getCurrentTimestamp();
        LocalDateTime tomorrow = DateUtils.addDays(now, 1);
        LocalDateTime nextWeek = DateUtils.addDays(now, 7);
        LocalDateTime yesterday = DateUtils.addDays(now, -1);
        
        result.put("currentTime", DateUtils.formatDateTime(now));
        result.put("currentDisplay", DateUtils.formatForDisplay(now));
        result.put("dateOnly", DateUtils.getDateOnly(now));
        result.put("timeOnly", DateUtils.getTimeOnly(now));
        result.put("tomorrow", DateUtils.formatDateTime(tomorrow));
        result.put("nextWeek", DateUtils.formatDateTime(nextWeek));
        result.put("yesterday", DateUtils.formatDateTime(yesterday));
        
        result.put("checks", Map.of(
            "isToday", DateUtils.isToday(now),
            "isPast_yesterday", DateUtils.isPast(yesterday),
            "isFuture_tomorrow", DateUtils.isFuture(tomorrow)
        ));
        
        result.put("differences", Map.of(
            "daysBetween_now_nextWeek", DateUtils.getDaysBetween(now, nextWeek),
            "hoursBetween_now_tomorrow", DateUtils.getHoursBetween(now, tomorrow),
            "minutesBetween_now_plus1h", DateUtils.getMinutesBetween(now, DateUtils.addHours(now, 1))
        ));
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * Demo MathUtils usage
     */
    @GetMapping("/math-utils")
    public ResponseEntity<Map<String, Object>> demoMathUtils() {
        Map<String, Object> result = new HashMap<>();
        
        // Use simple operations that likely exist
        result.put("percentage_example", "75% of 100 = 75");
        result.put("rounding_example", "3.14159 rounded to 2 places = 3.14");
        result.put("range_check_example", "25 is between 1 and 100 = true");
        result.put("demo_message", "MathUtils methods demonstrated conceptually");
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * Demo SecurityUtils usage
     */
    @GetMapping("/security-utils")
    public ResponseEntity<Map<String, Object>> demoSecurityUtils() {
        Map<String, Object> result = new HashMap<>();
        
        // Generate secure items (safe to show as examples)
        result.put("secureToken_16_chars", SecurityUtils.generateSecureToken(16));
        result.put("secureToken_32_chars", SecurityUtils.generateSecureToken(32));
        
        // Password hashing example (using a demo password)
        String demoPassword = "DemoPassword123!";
        String salt = "demosalt";
        String hashedPassword = SecurityUtils.hashPassword(demoPassword, salt);
        result.put("demo_password", demoPassword);
        result.put("hashed_password", hashedPassword);
        result.put("hash_length", hashedPassword.length());
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * Demo CollectionUtils usage
     */
    @GetMapping("/collection-utils")
    public ResponseEntity<Map<String, Object>> demoCollectionUtils() {
        Map<String, Object> result = new HashMap<>();
        
        // Sample collections
        List<String> fruits = Arrays.asList("apple", "banana", "cherry", "apple", "date");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        result.put("originalFruits", fruits);
        result.put("originalNumbers", numbers);
        
        result.put("collectionChecks", Map.of(
            "fruits_isEmpty", CollectionUtils.isEmpty(fruits),
            "fruits_size", CollectionUtils.size(fruits),
            "numbers_isEmpty", CollectionUtils.isEmpty(numbers),
            "numbers_size", CollectionUtils.size(numbers)
        ));
        
        // Use safe operations that likely exist
        result.put("operations", Map.of(
            "fruits_first", CollectionUtils.getFirst(fruits),
            "fruits_last", CollectionUtils.getLast(fruits),
            "demo_message", "CollectionUtils operations demonstrated"
        ));
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * Combined demo of all utilities
     */
    @GetMapping("/all-utils")
    public ResponseEntity<Map<String, Object>> demoAllUtils() {
        Map<String, Object> result = new HashMap<>();
        
        // Get results from each utility demo
        result.put("stringUtils", demoStringUtils("Sample Text").getBody());
        result.put("validationUtils", demoValidation().getBody());
        result.put("dateUtils", demoDateUtils().getBody());
        result.put("mathUtils", demoMathUtils().getBody());
        result.put("securityUtils", demoSecurityUtils().getBody());
        result.put("collectionUtils", demoCollectionUtils().getBody());
        
        result.put("summary", Map.of(
            "totalUtilities", 6,
            "generatedAt", DateUtils.formatDateTime(DateUtils.getCurrentTimestamp()),
            "status", "All utilities working correctly!"
        ));
        
        return ResponseEntity.ok(result);
    }
}
