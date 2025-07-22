package com.example.skill_sharing_backend.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

/**
 * Comprehensive mathematical utility class
 * Provides various mathematical operations and calculations
 */
public class MathUtils {
    
    private static final Random RANDOM = new Random();
    
    /**
     * Private constructor to prevent instantiation
     */
    private MathUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Check if a number is even
     * @param number number to check
     * @return true if even
     */
    public static boolean isEven(int number) {
        return number % 2 == 0;
    }
    
    /**
     * Check if a number is odd
     * @param number number to check
     * @return true if odd
     */
    public static boolean isOdd(int number) {
        return number % 2 != 0;
    }
    
    /**
     * Check if a number is prime
     * @param number number to check
     * @return true if prime
     */
    public static boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        
        if (number <= 3) {
            return true;
        }
        
        if (number % 2 == 0 || number % 3 == 0) {
            return false;
        }
        
        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Calculate factorial of a number
     * @param n number to calculate factorial for
     * @return factorial of n
     * @throws IllegalArgumentException if n is negative
     */
    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers");
        }
        
        if (n == 0 || n == 1) {
            return 1;
        }
        
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        
        return result;
    }
    
    /**
     * Calculate Fibonacci number at position n
     * @param n position in Fibonacci sequence
     * @return Fibonacci number at position n
     * @throws IllegalArgumentException if n is negative
     */
    public static long fibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Fibonacci is not defined for negative positions");
        }
        
        if (n == 0) return 0;
        if (n == 1) return 1;
        
        long a = 0, b = 1;
        for (int i = 2; i <= n; i++) {
            long temp = a + b;
            a = b;
            b = temp;
        }
        
        return b;
    }
    
    /**
     * Calculate greatest common divisor using Euclidean algorithm
     * @param a first number
     * @param b second number
     * @return GCD of a and b
     */
    public static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        
        return a;
    }
    
    /**
     * Calculate least common multiple
     * @param a first number
     * @param b second number
     * @return LCM of a and b
     */
    public static int lcm(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        
        return Math.abs(a * b) / gcd(a, b);
    }
    
    /**
     * Calculate power using fast exponentiation
     * @param base base number
     * @param exponent exponent
     * @return base raised to the power of exponent
     */
    public static long power(long base, int exponent) {
        if (exponent < 0) {
            throw new IllegalArgumentException("Exponent cannot be negative for integer calculations");
        }
        
        if (exponent == 0) {
            return 1;
        }
        
        long result = 1;
        base = base % Long.MAX_VALUE; // Handle overflow
        
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % Long.MAX_VALUE;
            }
            
            exponent = exponent >> 1;
            base = (base * base) % Long.MAX_VALUE;
        }
        
        return result;
    }
    
    /**
     * Calculate square root using Newton's method
     * @param number number to find square root of
     * @return square root
     * @throws IllegalArgumentException if number is negative
     */
    public static double sqrt(double number) {
        if (number < 0) {
            throw new IllegalArgumentException("Square root of negative number is not real");
        }
        
        if (number == 0 || number == 1) {
            return number;
        }
        
        double x = number;
        double root;
        
        while (true) {
            root = 0.5 * (x + number / x);
            
            if (Math.abs(root - x) < 0.000001) {
                break;
            }
            
            x = root;
        }
        
        return root;
    }
    
    /**
     * Calculate nth root
     * @param number number to find root of
     * @param n root degree
     * @return nth root of number
     */
    public static double nthRoot(double number, int n) {
        if (n == 0) {
            throw new IllegalArgumentException("Root degree cannot be zero");
        }
        
        if (n == 1) {
            return number;
        }
        
        if (n == 2) {
            return sqrt(number);
        }
        
        return Math.pow(number, 1.0 / n);
    }
    
    /**
     * Check if a number is perfect square
     * @param number number to check
     * @return true if perfect square
     */
    public static boolean isPerfectSquare(long number) {
        if (number < 0) {
            return false;
        }
        
        long sqrt = (long) Math.sqrt(number);
        return sqrt * sqrt == number;
    }
    
    /**
     * Round to specified decimal places
     * @param value value to round
     * @param places decimal places
     * @return rounded value
     */
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException("Decimal places cannot be negative");
        }
        
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    /**
     * Calculate percentage
     * @param part part value
     * @param total total value
     * @return percentage
     */
    public static double percentage(double part, double total) {
        if (total == 0) {
            throw new IllegalArgumentException("Total cannot be zero");
        }
        
        return (part / total) * 100;
    }
    
    /**
     * Calculate percentage change
     * @param oldValue old value
     * @param newValue new value
     * @return percentage change
     */
    public static double percentageChange(double oldValue, double newValue) {
        if (oldValue == 0) {
            throw new IllegalArgumentException("Old value cannot be zero");
        }
        
        return ((newValue - oldValue) / oldValue) * 100;
    }
    
    /**
     * Clamp value between min and max
     * @param value value to clamp
     * @param min minimum value
     * @param max maximum value
     * @return clamped value
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Clamp double value between min and max
     * @param value value to clamp
     * @param min minimum value
     * @param max maximum value
     * @return clamped value
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Linear interpolation between two values
     * @param start start value
     * @param end end value
     * @param t interpolation factor (0.0 to 1.0)
     * @return interpolated value
     */
    public static double lerp(double start, double end, double t) {
        return start + t * (end - start);
    }
    
    /**
     * Map value from one range to another
     * @param value value to map
     * @param inputMin input range minimum
     * @param inputMax input range maximum
     * @param outputMin output range minimum
     * @param outputMax output range maximum
     * @return mapped value
     */
    public static double map(double value, double inputMin, double inputMax, double outputMin, double outputMax) {
        return outputMin + (outputMax - outputMin) * ((value - inputMin) / (inputMax - inputMin));
    }
    
    /**
     * Generate random integer between min and max (inclusive)
     * @param min minimum value
     * @param max maximum value
     * @return random integer
     */
    public static int randomInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min cannot be greater than max");
        }
        
        return RANDOM.nextInt(max - min + 1) + min;
    }
    
    /**
     * Generate random double between min and max
     * @param min minimum value
     * @param max maximum value
     * @return random double
     */
    public static double randomDouble(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("Min cannot be greater than max");
        }
        
        return min + (max - min) * RANDOM.nextDouble();
    }
    
    /**
     * Calculate sum of array
     * @param numbers array of numbers
     * @return sum
     */
    public static double sum(double[] numbers) {
        if (numbers == null || numbers.length == 0) {
            return 0;
        }
        
        double sum = 0;
        for (double num : numbers) {
            sum += num;
        }
        
        return sum;
    }
    
    /**
     * Calculate sum of collection
     * @param numbers collection of numbers
     * @return sum
     */
    public static double sum(Collection<? extends Number> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }
        
        return numbers.stream()
                .mapToDouble(Number::doubleValue)
                .sum();
    }
    
    /**
     * Calculate average of array
     * @param numbers array of numbers
     * @return average
     */
    public static double average(double[] numbers) {
        if (numbers == null || numbers.length == 0) {
            return 0;
        }
        
        return sum(numbers) / numbers.length;
    }
    
    /**
     * Calculate average of collection
     * @param numbers collection of numbers
     * @return average
     */
    public static double average(Collection<? extends Number> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }
        
        return sum(numbers) / numbers.size();
    }
    
    /**
     * Find minimum value in array
     * @param numbers array of numbers
     * @return minimum value
     */
    public static double min(double[] numbers) {
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        
        double min = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] < min) {
                min = numbers[i];
            }
        }
        
        return min;
    }
    
    /**
     * Find maximum value in array
     * @param numbers array of numbers
     * @return maximum value
     */
    public static double max(double[] numbers) {
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        
        double max = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
            }
        }
        
        return max;
    }
    
    /**
     * Calculate median of array
     * @param numbers array of numbers
     * @return median value
     */
    public static double median(double[] numbers) {
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        
        double[] sorted = numbers.clone();
        Arrays.sort(sorted);
        
        int middle = sorted.length / 2;
        
        if (sorted.length % 2 == 0) {
            return (sorted[middle - 1] + sorted[middle]) / 2.0;
        } else {
            return sorted[middle];
        }
    }
    
    /**
     * Calculate standard deviation
     * @param numbers array of numbers
     * @return standard deviation
     */
    public static double standardDeviation(double[] numbers) {
        if (numbers == null || numbers.length == 0) {
            return 0;
        }
        
        double mean = average(numbers);
        double variance = 0;
        
        for (double num : numbers) {
            variance += Math.pow(num - mean, 2);
        }
        
        variance /= numbers.length;
        return Math.sqrt(variance);
    }
    
    /**
     * Calculate variance
     * @param numbers array of numbers
     * @return variance
     */
    public static double variance(double[] numbers) {
        if (numbers == null || numbers.length == 0) {
            return 0;
        }
        
        double mean = average(numbers);
        double variance = 0;
        
        for (double num : numbers) {
            variance += Math.pow(num - mean, 2);
        }
        
        return variance / numbers.length;
    }
    
    /**
     * Calculate distance between two points in 2D space
     * @param x1 x coordinate of first point
     * @param y1 y coordinate of first point
     * @param x2 x coordinate of second point
     * @param y2 y coordinate of second point
     * @return distance between points
     */
    public static double distance2D(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Calculate distance between two points in 3D space
     * @param x1 x coordinate of first point
     * @param y1 y coordinate of first point
     * @param z1 z coordinate of first point
     * @param x2 x coordinate of second point
     * @param y2 y coordinate of second point
     * @param z2 z coordinate of second point
     * @return distance between points
     */
    public static double distance3D(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dz = z2 - z1;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    /**
     * Convert degrees to radians
     * @param degrees angle in degrees
     * @return angle in radians
     */
    public static double toRadians(double degrees) {
        return Math.toRadians(degrees);
    }
    
    /**
     * Convert radians to degrees
     * @param radians angle in radians
     * @return angle in degrees
     */
    public static double toDegrees(double radians) {
        return Math.toDegrees(radians);
    }
    
    /**
     * Calculate hypotenuse of right triangle
     * @param a first side
     * @param b second side
     * @return hypotenuse
     */
    public static double hypotenuse(double a, double b) {
        return Math.sqrt(a * a + b * b);
    }
    
    /**
     * Calculate area of circle
     * @param radius radius of circle
     * @return area
     */
    public static double circleArea(double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be negative");
        }
        
        return Math.PI * radius * radius;
    }
    
    /**
     * Calculate circumference of circle
     * @param radius radius of circle
     * @return circumference
     */
    public static double circleCircumference(double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be negative");
        }
        
        return 2 * Math.PI * radius;
    }
    
    /**
     * Calculate area of rectangle
     * @param width width of rectangle
     * @param height height of rectangle
     * @return area
     */
    public static double rectangleArea(double width, double height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Width and height cannot be negative");
        }
        
        return width * height;
    }
    
    /**
     * Calculate perimeter of rectangle
     * @param width width of rectangle
     * @param height height of rectangle
     * @return perimeter
     */
    public static double rectanglePerimeter(double width, double height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Width and height cannot be negative");
        }
        
        return 2 * (width + height);
    }
    
    /**
     * Calculate area of triangle using Heron's formula
     * @param a first side
     * @param b second side
     * @param c third side
     * @return area
     */
    public static double triangleArea(double a, double b, double c) {
        if (a <= 0 || b <= 0 || c <= 0) {
            throw new IllegalArgumentException("Side lengths must be positive");
        }
        
        if (a + b <= c || a + c <= b || b + c <= a) {
            throw new IllegalArgumentException("Invalid triangle: sum of two sides must be greater than third side");
        }
        
        double s = (a + b + c) / 2; // semi-perimeter
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
    
    /**
     * Check if three sides can form a valid triangle
     * @param a first side
     * @param b second side
     * @param c third side
     * @return true if valid triangle
     */
    public static boolean isValidTriangle(double a, double b, double c) {
        return a > 0 && b > 0 && c > 0 && 
               a + b > c && a + c > b && b + c > a;
    }
    
    /**
     * Calculate compound interest
     * @param principal initial amount
     * @param rate annual interest rate (as decimal)
     * @param time time in years
     * @param compoundFrequency compounds per year
     * @return final amount
     */
    public static double compoundInterest(double principal, double rate, double time, int compoundFrequency) {
        if (principal < 0 || rate < 0 || time < 0 || compoundFrequency <= 0) {
            throw new IllegalArgumentException("Invalid parameters for compound interest calculation");
        }
        
        return principal * Math.pow(1 + rate / compoundFrequency, compoundFrequency * time);
    }
    
    /**
     * Calculate simple interest
     * @param principal initial amount
     * @param rate annual interest rate (as decimal)
     * @param time time in years
     * @return interest amount
     */
    public static double simpleInterest(double principal, double rate, double time) {
        if (principal < 0 || rate < 0 || time < 0) {
            throw new IllegalArgumentException("Invalid parameters for simple interest calculation");
        }
        
        return principal * rate * time;
    }
}
