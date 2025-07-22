package com.example.skill_sharing_backend.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service for processing and analyzing data collections
 * Provides advanced data processing capabilities
 */
@Service
public class DataProcessingService {
    
    /**
     * Process a collection of data with custom transformer
     * @param data input data collection
     * @param transformer transformation function
     * @param <T> input type
     * @param <R> output type
     * @return processed data
     */
    public <T, R> List<R> processData(Collection<T> data, Function<T, R> transformer) {
        if (data == null || transformer == null) {
            return new ArrayList<>();
        }
        
        return data.stream()
                .filter(Objects::nonNull)
                .map(transformer)
                .collect(Collectors.toList());
    }
    
    /**
     * Aggregate data by grouping key
     * @param data input data
     * @param keyExtractor key extraction function
     * @param <T> data type
     * @param <K> key type
     * @return grouped data
     */
    public <T, K> Map<K, List<T>> aggregateData(Collection<T> data, Function<T, K> keyExtractor) {
        if (data == null || keyExtractor == null) {
            return new HashMap<>();
        }
        
        return data.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(keyExtractor));
    }
    
    /**
     * Calculate statistics for numeric data
     * @param numbers collection of numbers
     * @return data statistics
     */
    public DataStatistics calculateStatistics(Collection<? extends Number> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return new DataStatistics();
        }
        
        double[] values = numbers.stream()
                .filter(Objects::nonNull)
                .mapToDouble(Number::doubleValue)
                .toArray();
                
        if (values.length == 0) {
            return new DataStatistics();
        }
        
        Arrays.sort(values);
        
        double sum = Arrays.stream(values).sum();
        double mean = sum / values.length;
        
        double variance = Arrays.stream(values)
                .map(x -> Math.pow(x - mean, 2))
                .average()
                .orElse(0.0);
                
        double standardDeviation = Math.sqrt(variance);
        
        double median;
        if (values.length % 2 == 0) {
            median = (values[values.length / 2 - 1] + values[values.length / 2]) / 2.0;
        } else {
            median = values[values.length / 2];
        }
        
        return new DataStatistics(
                values.length,
                sum,
                mean,
                median,
                values[0], // min
                values[values.length - 1], // max
                variance,
                standardDeviation
        );
    }
    
    /**
     * Find outliers using IQR method
     * @param numbers collection of numbers
     * @return list of outlier values
     */
    public List<Double> findOutliers(Collection<? extends Number> numbers) {
        if (numbers == null || numbers.size() < 4) {
            return new ArrayList<>();
        }
        
        double[] values = numbers.stream()
                .filter(Objects::nonNull)
                .mapToDouble(Number::doubleValue)
                .sorted()
                .toArray();
                
        if (values.length < 4) {
            return new ArrayList<>();
        }
        
        int n = values.length;
        double q1 = values[n / 4];
        double q3 = values[3 * n / 4];
        double iqr = q3 - q1;
        
        double lowerBound = q1 - 1.5 * iqr;
        double upperBound = q3 + 1.5 * iqr;
        
        return Arrays.stream(values)
                .filter(x -> x < lowerBound || x > upperBound)
                .boxed()
                .collect(Collectors.toList());
    }
    
    /**
     * Normalize data to 0-1 range
     * @param numbers collection of numbers
     * @return normalized values
     */
    public List<Double> normalizeData(Collection<? extends Number> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return new ArrayList<>();
        }
        
        double[] values = numbers.stream()
                .filter(Objects::nonNull)
                .mapToDouble(Number::doubleValue)
                .toArray();
                
        if (values.length == 0) {
            return new ArrayList<>();
        }
        
        double min = Arrays.stream(values).min().orElse(0.0);
        double max = Arrays.stream(values).max().orElse(0.0);
        
        if (min == max) {
            return Arrays.stream(values)
                    .boxed()
                    .map(x -> 0.0)
                    .collect(Collectors.toList());
        }
        
        return Arrays.stream(values)
                .map(x -> (x - min) / (max - min))
                .boxed()
                .collect(Collectors.toList());
    }
    
    /**
     * Calculate correlation coefficient between two datasets
     * @param x first dataset
     * @param y second dataset
     * @return correlation coefficient (-1 to 1)
     */
    public double calculateCorrelation(List<? extends Number> x, List<? extends Number> y) {
        if (x == null || y == null || x.size() != y.size() || x.isEmpty()) {
            return 0.0;
        }
        
        int n = x.size();
        double sumX = x.stream().mapToDouble(Number::doubleValue).sum();
        double sumY = y.stream().mapToDouble(Number::doubleValue).sum();
        double sumXY = 0.0;
        double sumXX = 0.0;
        double sumYY = 0.0;
        
        for (int i = 0; i < n; i++) {
            double xi = x.get(i).doubleValue();
            double yi = y.get(i).doubleValue();
            
            sumXY += xi * yi;
            sumXX += xi * xi;
            sumYY += yi * yi;
        }
        
        double numerator = n * sumXY - sumX * sumY;
        double denominator = Math.sqrt((n * sumXX - sumX * sumX) * (n * sumYY - sumY * sumY));
        
        return denominator == 0 ? 0 : numerator / denominator;
    }
    
    /**
     * Calculate moving average
     * @param data input data
     * @param windowSize window size for moving average
     * @return moving averages
     */
    public List<Double> calculateMovingAverage(List<? extends Number> data, int windowSize) {
        if (data == null || data.isEmpty() || windowSize <= 0 || windowSize > data.size()) {
            return new ArrayList<>();
        }
        
        List<Double> movingAverages = new ArrayList<>();
        
        for (int i = 0; i <= data.size() - windowSize; i++) {
            double sum = 0.0;
            for (int j = i; j < i + windowSize; j++) {
                sum += data.get(j).doubleValue();
            }
            movingAverages.add(sum / windowSize);
        }
        
        return movingAverages;
    }
    
    /**
     * Detect trends in time series data
     * @param data time series data
     * @return trend analysis result
     */
    public TrendAnalysis analyzeTrend(List<? extends Number> data) {
        if (data == null || data.size() < 2) {
            return new TrendAnalysis(TrendDirection.FLAT, 0.0, 0.0);
        }
        
        List<Double> values = data.stream()
                .map(Number::doubleValue)
                .collect(Collectors.toList());
        
        // Calculate linear regression slope
        int n = values.size();
        double sumX = n * (n - 1) / 2.0; // 0 + 1 + 2 + ... + (n-1)
        double sumY = values.stream().mapToDouble(Double::doubleValue).sum();
        double sumXY = 0.0;
        double sumXX = n * (n - 1) * (2 * n - 1) / 6.0; // 0² + 1² + 2² + ... + (n-1)²
        
        for (int i = 0; i < n; i++) {
            sumXY += i * values.get(i);
        }
        
        double slope = (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);
        double intercept = (sumY - slope * sumX) / n;
        
        TrendDirection direction;
        if (Math.abs(slope) < 0.001) { // threshold for flat trend
            direction = TrendDirection.FLAT;
        } else if (slope > 0) {
            direction = TrendDirection.INCREASING;
        } else {
            direction = TrendDirection.DECREASING;
        }
        
        // Calculate R-squared (coefficient of determination)
        double meanY = sumY / n;
        double ssTotal = values.stream()
                .mapToDouble(y -> Math.pow(y - meanY, 2))
                .sum();
        
        double ssResidual = 0.0;
        for (int i = 0; i < n; i++) {
            double predicted = intercept + slope * i;
            ssResidual += Math.pow(values.get(i) - predicted, 2);
        }
        
        double rSquared = ssTotal == 0 ? 1.0 : 1 - (ssResidual / ssTotal);
        
        return new TrendAnalysis(direction, slope, rSquared);
    }
    
    /**
     * Find data patterns using frequency analysis
     * @param data input data
     * @param <T> data type
     * @return pattern analysis result
     */
    public <T> PatternAnalysis<T> analyzePatterns(Collection<T> data) {
        if (data == null || data.isEmpty()) {
            return new PatternAnalysis<>();
        }
        
        Map<T, Long> frequencies = data.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
        
        T mostFrequent = frequencies.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        
        T leastFrequent = frequencies.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        
        long maxFrequency = frequencies.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);
        
        long minFrequency = frequencies.values().stream()
                .mapToLong(Long::longValue)
                .min()
                .orElse(0L);
        
        int uniqueCount = frequencies.size();
        double entropy = calculateEntropy(frequencies.values());
        
        return new PatternAnalysis<>(
                frequencies,
                mostFrequent,
                leastFrequent,
                maxFrequency,
                minFrequency,
                uniqueCount,
                entropy
        );
    }
    
    /**
     * Calculate entropy of frequency distribution
     * @param frequencies collection of frequencies
     * @return entropy value
     */
    private double calculateEntropy(Collection<Long> frequencies) {
        long total = frequencies.stream().mapToLong(Long::longValue).sum();
        
        if (total == 0) {
            return 0.0;
        }
        
        return frequencies.stream()
                .mapToDouble(freq -> {
                    if (freq == 0) return 0.0;
                    double probability = (double) freq / total;
                    return -probability * Math.log(probability) / Math.log(2);
                })
                .sum();
    }
    
    /**
     * Data statistics container class
     */
    public static class DataStatistics {
        private final int count;
        private final double sum;
        private final double mean;
        private final double median;
        private final double min;
        private final double max;
        private final double variance;
        private final double standardDeviation;
        
        public DataStatistics() {
            this(0, 0, 0, 0, 0, 0, 0, 0);
        }
        
        public DataStatistics(int count, double sum, double mean, double median,
                             double min, double max, double variance, double standardDeviation) {
            this.count = count;
            this.sum = sum;
            this.mean = mean;
            this.median = median;
            this.min = min;
            this.max = max;
            this.variance = variance;
            this.standardDeviation = standardDeviation;
        }
        
        // Getters
        public int getCount() { return count; }
        public double getSum() { return sum; }
        public double getMean() { return mean; }
        public double getMedian() { return median; }
        public double getMin() { return min; }
        public double getMax() { return max; }
        public double getVariance() { return variance; }
        public double getStandardDeviation() { return standardDeviation; }
        
        @Override
        public String toString() {
            return String.format(
                "DataStatistics{count=%d, sum=%.2f, mean=%.2f, median=%.2f, " +
                "min=%.2f, max=%.2f, variance=%.2f, stdDev=%.2f}",
                count, sum, mean, median, min, max, variance, standardDeviation
            );
        }
    }
    
    /**
     * Trend analysis result
     */
    public static class TrendAnalysis {
        private final TrendDirection direction;
        private final double slope;
        private final double strength; // R-squared value
        
        public TrendAnalysis(TrendDirection direction, double slope, double strength) {
            this.direction = direction;
            this.slope = slope;
            this.strength = Math.max(0, Math.min(1, strength)); // Clamp to [0,1]
        }
        
        public TrendDirection getDirection() { return direction; }
        public double getSlope() { return slope; }
        public double getStrength() { return strength; }
        
        @Override
        public String toString() {
            return String.format("TrendAnalysis{direction=%s, slope=%.4f, strength=%.4f}",
                    direction, slope, strength);
        }
    }
    
    /**
     * Trend direction enumeration
     */
    public enum TrendDirection {
        INCREASING,
        DECREASING,
        FLAT
    }
    
    /**
     * Pattern analysis result
     * @param <T> data type
     */
    public static class PatternAnalysis<T> {
        private final Map<T, Long> frequencies;
        private final T mostFrequent;
        private final T leastFrequent;
        private final long maxFrequency;
        private final long minFrequency;
        private final int uniqueCount;
        private final double entropy;
        
        public PatternAnalysis() {
            this(new HashMap<>(), null, null, 0, 0, 0, 0.0);
        }
        
        public PatternAnalysis(Map<T, Long> frequencies, T mostFrequent, T leastFrequent,
                              long maxFrequency, long minFrequency, int uniqueCount, double entropy) {
            this.frequencies = frequencies;
            this.mostFrequent = mostFrequent;
            this.leastFrequent = leastFrequent;
            this.maxFrequency = maxFrequency;
            this.minFrequency = minFrequency;
            this.uniqueCount = uniqueCount;
            this.entropy = entropy;
        }
        
        // Getters
        public Map<T, Long> getFrequencies() { return frequencies; }
        public T getMostFrequent() { return mostFrequent; }
        public T getLeastFrequent() { return leastFrequent; }
        public long getMaxFrequency() { return maxFrequency; }
        public long getMinFrequency() { return minFrequency; }
        public int getUniqueCount() { return uniqueCount; }
        public double getEntropy() { return entropy; }
        
        @Override
        public String toString() {
            return String.format(
                "PatternAnalysis{mostFrequent=%s, leastFrequent=%s, " +
                "maxFreq=%d, minFreq=%d, uniqueCount=%d, entropy=%.4f}",
                mostFrequent, leastFrequent, maxFrequency, minFrequency, uniqueCount, entropy
            );
        }
    }
}
