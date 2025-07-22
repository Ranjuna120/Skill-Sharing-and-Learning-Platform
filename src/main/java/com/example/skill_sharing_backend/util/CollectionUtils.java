package com.example.skill_sharing_backend.util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Comprehensive collection utility class
 * Provides various collection manipulation and helper methods
 */
public class CollectionUtils {
    
    /**
     * Private constructor to prevent instantiation
     */
    private CollectionUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Check if collection is null or empty
     * @param collection collection to check
     * @return true if null or empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    
    /**
     * Check if collection is not null and not empty
     * @param collection collection to check
     * @return true if not empty
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
    
    /**
     * Get size of collection (safe for null)
     * @param collection collection to check
     * @return size of collection or 0 if null
     */
    public static int size(Collection<?> collection) {
        return collection != null ? collection.size() : 0;
    }
    
    /**
     * Get first element from collection
     * @param collection collection to get from
     * @param <T> element type
     * @return first element or null if empty
     */
    public static <T> T getFirst(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        return collection.iterator().next();
    }
    
    /**
     * Get last element from list
     * @param list list to get from
     * @param <T> element type
     * @return last element or null if empty
     */
    public static <T> T getLast(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(list.size() - 1);
    }
    
    /**
     * Safe get from list with index bounds checking
     * @param list list to get from
     * @param index index to get
     * @param <T> element type
     * @return element at index or null if out of bounds
     */
    public static <T> T safeGet(List<T> list, int index) {
        if (list == null || index < 0 || index >= list.size()) {
            return null;
        }
        return list.get(index);
    }
    
    /**
     * Safe get from list with default value
     * @param list list to get from
     * @param index index to get
     * @param defaultValue default value if out of bounds
     * @param <T> element type
     * @return element at index or default value
     */
    public static <T> T safeGet(List<T> list, int index, T defaultValue) {
        T result = safeGet(list, index);
        return result != null ? result : defaultValue;
    }
    
    /**
     * Filter collection by predicate
     * @param collection collection to filter
     * @param predicate filter predicate
     * @param <T> element type
     * @return filtered list
     */
    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection) || predicate == null) {
            return new ArrayList<>();
        }
        
        return collection.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
    
    /**
     * Transform collection using mapper function
     * @param collection collection to transform
     * @param mapper transformation function
     * @param <T> input type
     * @param <R> output type
     * @return transformed list
     */
    public static <T, R> List<R> map(Collection<T> collection, Function<T, R> mapper) {
        if (isEmpty(collection) || mapper == null) {
            return new ArrayList<>();
        }
        
        return collection.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
    
    /**
     * Find first element matching predicate
     * @param collection collection to search
     * @param predicate search predicate
     * @param <T> element type
     * @return first matching element or null
     */
    public static <T> T findFirst(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection) || predicate == null) {
            return null;
        }
        
        return collection.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Check if collection contains any element matching predicate
     * @param collection collection to check
     * @param predicate match predicate
     * @param <T> element type
     * @return true if any element matches
     */
    public static <T> boolean anyMatch(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection) || predicate == null) {
            return false;
        }
        
        return collection.stream().anyMatch(predicate);
    }
    
    /**
     * Check if all elements match predicate
     * @param collection collection to check
     * @param predicate match predicate
     * @param <T> element type
     * @return true if all elements match
     */
    public static <T> boolean allMatch(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection) || predicate == null) {
            return true;
        }
        
        return collection.stream().allMatch(predicate);
    }
    
    /**
     * Count elements matching predicate
     * @param collection collection to count
     * @param predicate count predicate
     * @param <T> element type
     * @return count of matching elements
     */
    public static <T> long count(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection) || predicate == null) {
            return 0;
        }
        
        return collection.stream().filter(predicate).count();
    }
    
    /**
     * Group collection by classifier function
     * @param collection collection to group
     * @param classifier grouping function
     * @param <T> element type
     * @param <K> key type
     * @return grouped map
     */
    public static <T, K> Map<K, List<T>> groupBy(Collection<T> collection, Function<T, K> classifier) {
        if (isEmpty(collection) || classifier == null) {
            return new HashMap<>();
        }
        
        return collection.stream()
                .collect(Collectors.groupingBy(classifier));
    }
    
    /**
     * Create a map from collection using key mapper
     * @param collection collection to convert
     * @param keyMapper key extraction function
     * @param <T> element type
     * @param <K> key type
     * @return map with elements as values
     */
    public static <T, K> Map<K, T> toMap(Collection<T> collection, Function<T, K> keyMapper) {
        if (isEmpty(collection) || keyMapper == null) {
            return new HashMap<>();
        }
        
        return collection.stream()
                .collect(Collectors.toMap(keyMapper, Function.identity()));
    }
    
    /**
     * Create a map from collection using key and value mappers
     * @param collection collection to convert
     * @param keyMapper key extraction function
     * @param valueMapper value extraction function
     * @param <T> element type
     * @param <K> key type
     * @param <V> value type
     * @return mapped result
     */
    public static <T, K, V> Map<K, V> toMap(Collection<T> collection, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        if (isEmpty(collection) || keyMapper == null || valueMapper == null) {
            return new HashMap<>();
        }
        
        return collection.stream()
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }
    
    /**
     * Remove duplicates from collection
     * @param collection collection with duplicates
     * @param <T> element type
     * @return list without duplicates (preserves order)
     */
    public static <T> List<T> removeDuplicates(Collection<T> collection) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        
        return collection.stream()
                .distinct()
                .collect(Collectors.toList());
    }
    
    /**
     * Remove duplicates based on key extractor
     * @param collection collection with duplicates
     * @param keyExtractor key extraction function
     * @param <T> element type
     * @param <K> key type
     * @return list without duplicates
     */
    public static <T, K> List<T> removeDuplicatesBy(Collection<T> collection, Function<T, K> keyExtractor) {
        if (isEmpty(collection) || keyExtractor == null) {
            return new ArrayList<>();
        }
        
        Set<K> seen = new HashSet<>();
        return collection.stream()
                .filter(item -> seen.add(keyExtractor.apply(item)))
                .collect(Collectors.toList());
    }
    
    /**
     * Partition collection into two lists based on predicate
     * @param collection collection to partition
     * @param predicate partitioning predicate
     * @param <T> element type
     * @return map with true/false keys for matching/non-matching elements
     */
    public static <T> Map<Boolean, List<T>> partition(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection) || predicate == null) {
            Map<Boolean, List<T>> result = new HashMap<>();
            result.put(true, new ArrayList<>());
            result.put(false, new ArrayList<>());
            return result;
        }
        
        return collection.stream()
                .collect(Collectors.partitioningBy(predicate));
    }
    
    /**
     * Split collection into chunks of specified size
     * @param collection collection to split
     * @param chunkSize chunk size
     * @param <T> element type
     * @return list of chunks
     */
    public static <T> List<List<T>> chunk(Collection<T> collection, int chunkSize) {
        if (isEmpty(collection) || chunkSize <= 0) {
            return new ArrayList<>();
        }
        
        List<T> list = new ArrayList<>(collection);
        List<List<T>> chunks = new ArrayList<>();
        
        for (int i = 0; i < list.size(); i += chunkSize) {
            int end = Math.min(i + chunkSize, list.size());
            chunks.add(new ArrayList<>(list.subList(i, end)));
        }
        
        return chunks;
    }
    
    /**
     * Flatten nested collections
     * @param collections collection of collections
     * @param <T> element type
     * @return flattened list
     */
    public static <T> List<T> flatten(Collection<? extends Collection<T>> collections) {
        if (isEmpty(collections)) {
            return new ArrayList<>();
        }
        
        return collections.stream()
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
    
    /**
     * Get intersection of two collections
     * @param collection1 first collection
     * @param collection2 second collection
     * @param <T> element type
     * @return intersection as set
     */
    public static <T> Set<T> intersection(Collection<T> collection1, Collection<T> collection2) {
        if (isEmpty(collection1) || isEmpty(collection2)) {
            return new HashSet<>();
        }
        
        Set<T> result = new HashSet<>(collection1);
        result.retainAll(collection2);
        return result;
    }
    
    /**
     * Get union of two collections
     * @param collection1 first collection
     * @param collection2 second collection
     * @param <T> element type
     * @return union as set
     */
    public static <T> Set<T> union(Collection<T> collection1, Collection<T> collection2) {
        Set<T> result = new HashSet<>();
        
        if (isNotEmpty(collection1)) {
            result.addAll(collection1);
        }
        
        if (isNotEmpty(collection2)) {
            result.addAll(collection2);
        }
        
        return result;
    }
    
    /**
     * Get difference of two collections (elements in first but not in second)
     * @param collection1 first collection
     * @param collection2 second collection
     * @param <T> element type
     * @return difference as set
     */
    public static <T> Set<T> difference(Collection<T> collection1, Collection<T> collection2) {
        if (isEmpty(collection1)) {
            return new HashSet<>();
        }
        
        Set<T> result = new HashSet<>(collection1);
        
        if (isNotEmpty(collection2)) {
            result.removeAll(collection2);
        }
        
        return result;
    }
    
    /**
     * Reverse a list
     * @param list list to reverse
     * @param <T> element type
     * @return new reversed list
     */
    public static <T> List<T> reverse(List<T> list) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        
        List<T> reversed = new ArrayList<>(list);
        Collections.reverse(reversed);
        return reversed;
    }
    
    /**
     * Shuffle a list
     * @param list list to shuffle
     * @param <T> element type
     * @return new shuffled list
     */
    public static <T> List<T> shuffle(List<T> list) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        
        List<T> shuffled = new ArrayList<>(list);
        Collections.shuffle(shuffled);
        return shuffled;
    }
    
    /**
     * Get random element from collection
     * @param collection collection to pick from
     * @param <T> element type
     * @return random element or null if empty
     */
    public static <T> T random(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        
        List<T> list = new ArrayList<>(collection);
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }
    
    /**
     * Get multiple random elements from collection
     * @param collection collection to pick from
     * @param count number of elements to pick
     * @param <T> element type
     * @return random elements
     */
    public static <T> List<T> randomSample(Collection<T> collection, int count) {
        if (isEmpty(collection) || count <= 0) {
            return new ArrayList<>();
        }
        
        List<T> list = new ArrayList<>(collection);
        Collections.shuffle(list);
        
        int sampleSize = Math.min(count, list.size());
        return list.subList(0, sampleSize);
    }
    
    /**
     * Join collection elements into string
     * @param collection collection to join
     * @param delimiter delimiter to use
     * @param <T> element type
     * @return joined string
     */
    public static <T> String join(Collection<T> collection, String delimiter) {
        if (isEmpty(collection)) {
            return "";
        }
        
        return collection.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.joining(delimiter == null ? "" : delimiter));
    }
    
    /**
     * Create frequency map from collection
     * @param collection collection to analyze
     * @param <T> element type
     * @return frequency map
     */
    public static <T> Map<T, Long> frequencies(Collection<T> collection) {
        if (isEmpty(collection)) {
            return new HashMap<>();
        }
        
        return collection.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
    
    /**
     * Get most frequent element
     * @param collection collection to analyze
     * @param <T> element type
     * @return most frequent element or null
     */
    public static <T> T mostFrequent(Collection<T> collection) {
        Map<T, Long> frequencies = frequencies(collection);
        
        return frequencies.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
    
    /**
     * Check if two collections have the same elements (ignoring order)
     * @param collection1 first collection
     * @param collection2 second collection
     * @param <T> element type
     * @return true if same elements
     */
    public static <T> boolean haveSameElements(Collection<T> collection1, Collection<T> collection2) {
        if (collection1 == collection2) {
            return true;
        }
        
        if (collection1 == null || collection2 == null) {
            return false;
        }
        
        if (collection1.size() != collection2.size()) {
            return false;
        }
        
        Map<T, Long> freq1 = frequencies(collection1);
        Map<T, Long> freq2 = frequencies(collection2);
        
        return freq1.equals(freq2);
    }
    
    /**
     * Zip two collections into pairs
     * @param collection1 first collection
     * @param collection2 second collection
     * @param <T> first element type
     * @param <U> second element type
     * @return list of pairs
     */
    public static <T, U> List<Pair<T, U>> zip(Collection<T> collection1, Collection<U> collection2) {
        if (isEmpty(collection1) || isEmpty(collection2)) {
            return new ArrayList<>();
        }
        
        Iterator<T> iter1 = collection1.iterator();
        Iterator<U> iter2 = collection2.iterator();
        List<Pair<T, U>> result = new ArrayList<>();
        
        while (iter1.hasNext() && iter2.hasNext()) {
            result.add(new Pair<>(iter1.next(), iter2.next()));
        }
        
        return result;
    }
    
    /**
     * Simple pair class for zip operations
     * @param <T> first element type
     * @param <U> second element type
     */
    public static class Pair<T, U> {
        private final T first;
        private final U second;
        
        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
        
        public T getFirst() { return first; }
        public U getSecond() { return second; }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            
            Pair<?, ?> pair = (Pair<?, ?>) obj;
            return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
        
        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
    }
}
