package com.example.skill_sharing_backend.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Comprehensive file I/O utility class
 * Provides various file operations and utilities
 */
public class FileUtils {
    
    /**
     * Private constructor to prevent instantiation
     */
    private FileUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Read entire file content as string
     * @param filePath path to file
     * @return file content as string
     * @throws IOException if file cannot be read
     */
    public static String readFileToString(String filePath) throws IOException {
        return readFileToString(Paths.get(filePath), StandardCharsets.UTF_8);
    }
    
    /**
     * Read entire file content as string with specified charset
     * @param filePath path to file
     * @param charset character encoding
     * @return file content as string
     * @throws IOException if file cannot be read
     */
    public static String readFileToString(Path filePath, Charset charset) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        
        return Files.readString(filePath, charset);
    }
    
    /**
     * Read file lines into list
     * @param filePath path to file
     * @return list of lines
     * @throws IOException if file cannot be read
     */
    public static List<String> readLines(String filePath) throws IOException {
        return readLines(Paths.get(filePath), StandardCharsets.UTF_8);
    }
    
    /**
     * Read file lines into list with specified charset
     * @param filePath path to file
     * @param charset character encoding
     * @return list of lines
     * @throws IOException if file cannot be read
     */
    public static List<String> readLines(Path filePath, Charset charset) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        
        return Files.readAllLines(filePath, charset);
    }
    
    /**
     * Write string content to file
     * @param filePath path to file
     * @param content content to write
     * @throws IOException if file cannot be written
     */
    public static void writeStringToFile(String filePath, String content) throws IOException {
        writeStringToFile(Paths.get(filePath), content, StandardCharsets.UTF_8, false);
    }
    
    /**
     * Write string content to file with options
     * @param filePath path to file
     * @param content content to write
     * @param charset character encoding
     * @param append whether to append to file
     * @throws IOException if file cannot be written
     */
    public static void writeStringToFile(Path filePath, String content, Charset charset, boolean append) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        
        if (content == null) {
            content = "";
        }
        
        // Create parent directories if they don't exist
        createDirectories(filePath.getParent());
        
        OpenOption[] options = append ? 
            new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND} :
            new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
            
        Files.write(filePath, content.getBytes(charset), options);
    }
    
    /**
     * Write lines to file
     * @param filePath path to file
     * @param lines lines to write
     * @throws IOException if file cannot be written
     */
    public static void writeLines(String filePath, List<String> lines) throws IOException {
        writeLines(Paths.get(filePath), lines, StandardCharsets.UTF_8, false);
    }
    
    /**
     * Write lines to file with options
     * @param filePath path to file
     * @param lines lines to write
     * @param charset character encoding
     * @param append whether to append to file
     * @throws IOException if file cannot be written
     */
    public static void writeLines(Path filePath, List<String> lines, Charset charset, boolean append) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        
        if (lines == null) {
            lines = new ArrayList<>();
        }
        
        // Create parent directories if they don't exist
        createDirectories(filePath.getParent());
        
        OpenOption[] options = append ? 
            new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND} :
            new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
            
        Files.write(filePath, lines, charset, options);
    }
    
    /**
     * Append string to file
     * @param filePath path to file
     * @param content content to append
     * @throws IOException if file cannot be written
     */
    public static void appendStringToFile(String filePath, String content) throws IOException {
        writeStringToFile(Paths.get(filePath), content, StandardCharsets.UTF_8, true);
    }
    
    /**
     * Copy file from source to destination
     * @param source source file path
     * @param destination destination file path
     * @throws IOException if copy fails
     */
    public static void copyFile(String source, String destination) throws IOException {
        copyFile(Paths.get(source), Paths.get(destination), false);
    }
    
    /**
     * Copy file from source to destination with options
     * @param source source file path
     * @param destination destination file path
     * @param replaceExisting whether to replace if destination exists
     * @throws IOException if copy fails
     */
    public static void copyFile(Path source, Path destination, boolean replaceExisting) throws IOException {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Source and destination paths cannot be null");
        }
        
        if (!Files.exists(source)) {
            throw new FileNotFoundException("Source file not found: " + source);
        }
        
        // Create parent directories if they don't exist
        createDirectories(destination.getParent());
        
        CopyOption[] options = replaceExisting ? 
            new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} :
            new CopyOption[0];
            
        Files.copy(source, destination, options);
    }
    
    /**
     * Move/rename file from source to destination
     * @param source source file path
     * @param destination destination file path
     * @throws IOException if move fails
     */
    public static void moveFile(String source, String destination) throws IOException {
        moveFile(Paths.get(source), Paths.get(destination), false);
    }
    
    /**
     * Move/rename file from source to destination with options
     * @param source source file path
     * @param destination destination file path
     * @param replaceExisting whether to replace if destination exists
     * @throws IOException if move fails
     */
    public static void moveFile(Path source, Path destination, boolean replaceExisting) throws IOException {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Source and destination paths cannot be null");
        }
        
        if (!Files.exists(source)) {
            throw new FileNotFoundException("Source file not found: " + source);
        }
        
        // Create parent directories if they don't exist
        createDirectories(destination.getParent());
        
        CopyOption[] options = replaceExisting ? 
            new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} :
            new CopyOption[0];
            
        Files.move(source, destination, options);
    }
    
    /**
     * Delete file
     * @param filePath path to file to delete
     * @return true if file was deleted, false if it didn't exist
     * @throws IOException if deletion fails
     */
    public static boolean deleteFile(String filePath) throws IOException {
        return deleteFile(Paths.get(filePath));
    }
    
    /**
     * Delete file
     * @param filePath path to file to delete
     * @return true if file was deleted, false if it didn't exist
     * @throws IOException if deletion fails
     */
    public static boolean deleteFile(Path filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        
        return Files.deleteIfExists(filePath);
    }
    
    /**
     * Delete directory and all its contents
     * @param dirPath path to directory to delete
     * @throws IOException if deletion fails
     */
    public static void deleteDirectory(String dirPath) throws IOException {
        deleteDirectory(Paths.get(dirPath));
    }
    
    /**
     * Delete directory and all its contents
     * @param dirPath path to directory to delete
     * @throws IOException if deletion fails
     */
    public static void deleteDirectory(Path dirPath) throws IOException {
        if (dirPath == null) {
            throw new IllegalArgumentException("Directory path cannot be null");
        }
        
        if (!Files.exists(dirPath)) {
            return;
        }
        
        if (!Files.isDirectory(dirPath)) {
            throw new IllegalArgumentException("Path is not a directory: " + dirPath);
        }
        
        Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
            
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    
    /**
     * Create directories (including parent directories)
     * @param dirPath path to directory to create
     * @throws IOException if creation fails
     */
    public static void createDirectories(String dirPath) throws IOException {
        if (dirPath != null) {
            createDirectories(Paths.get(dirPath));
        }
    }
    
    /**
     * Create directories (including parent directories)
     * @param dirPath path to directory to create
     * @throws IOException if creation fails
     */
    public static void createDirectories(Path dirPath) throws IOException {
        if (dirPath != null && !Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
    }
    
    /**
     * Check if file exists
     * @param filePath path to file
     * @return true if file exists
     */
    public static boolean exists(String filePath) {
        return filePath != null && Files.exists(Paths.get(filePath));
    }
    
    /**
     * Check if file exists
     * @param filePath path to file
     * @return true if file exists
     */
    public static boolean exists(Path filePath) {
        return filePath != null && Files.exists(filePath);
    }
    
    /**
     * Check if path is a file
     * @param filePath path to check
     * @return true if it's a file
     */
    public static boolean isFile(String filePath) {
        return filePath != null && Files.isRegularFile(Paths.get(filePath));
    }
    
    /**
     * Check if path is a file
     * @param filePath path to check
     * @return true if it's a file
     */
    public static boolean isFile(Path filePath) {
        return filePath != null && Files.isRegularFile(filePath);
    }
    
    /**
     * Check if path is a directory
     * @param dirPath path to check
     * @return true if it's a directory
     */
    public static boolean isDirectory(String dirPath) {
        return dirPath != null && Files.isDirectory(Paths.get(dirPath));
    }
    
    /**
     * Check if path is a directory
     * @param dirPath path to check
     * @return true if it's a directory
     */
    public static boolean isDirectory(Path dirPath) {
        return dirPath != null && Files.isDirectory(dirPath);
    }
    
    /**
     * Get file size in bytes
     * @param filePath path to file
     * @return file size in bytes
     * @throws IOException if file cannot be accessed
     */
    public static long getFileSize(String filePath) throws IOException {
        return getFileSize(Paths.get(filePath));
    }
    
    /**
     * Get file size in bytes
     * @param filePath path to file
     * @return file size in bytes
     * @throws IOException if file cannot be accessed
     */
    public static long getFileSize(Path filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        
        return Files.size(filePath);
    }
    
    /**
     * Get human-readable file size
     * @param filePath path to file
     * @return human-readable file size
     * @throws IOException if file cannot be accessed
     */
    public static String getHumanReadableFileSize(String filePath) throws IOException {
        return getHumanReadableFileSize(getFileSize(filePath));
    }
    
    /**
     * Convert bytes to human-readable format
     * @param bytes number of bytes
     * @return human-readable format
     */
    public static String getHumanReadableFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        
        String[] units = {"KB", "MB", "GB", "TB", "PB"};
        double size = bytes;
        int unitIndex = -1;
        
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        
        return String.format("%.2f %s", size, units[unitIndex]);
    }
    
    /**
     * Get file extension
     * @param filePath path to file
     * @return file extension (without dot) or empty string if no extension
     */
    public static String getFileExtension(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return "";
        }
        
        String fileName = Paths.get(filePath).getFileName().toString();
        int lastDotIndex = fileName.lastIndexOf('.');
        
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }
        
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
    
    /**
     * Get file name without extension
     * @param filePath path to file
     * @return file name without extension
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return "";
        }
        
        String fileName = Paths.get(filePath).getFileName().toString();
        int lastDotIndex = fileName.lastIndexOf('.');
        
        if (lastDotIndex == -1) {
            return fileName;
        }
        
        return fileName.substring(0, lastDotIndex);
    }
    
    /**
     * List files in directory
     * @param dirPath directory path
     * @return list of file paths
     * @throws IOException if directory cannot be read
     */
    public static List<Path> listFiles(String dirPath) throws IOException {
        return listFiles(Paths.get(dirPath), false);
    }
    
    /**
     * List files in directory with recursive option
     * @param dirPath directory path
     * @param recursive whether to include subdirectories
     * @return list of file paths
     * @throws IOException if directory cannot be read
     */
    public static List<Path> listFiles(Path dirPath, boolean recursive) throws IOException {
        if (dirPath == null) {
            throw new IllegalArgumentException("Directory path cannot be null");
        }
        
        if (!Files.exists(dirPath)) {
            throw new FileNotFoundException("Directory not found: " + dirPath);
        }
        
        if (!Files.isDirectory(dirPath)) {
            throw new IllegalArgumentException("Path is not a directory: " + dirPath);
        }
        
        List<Path> files = new ArrayList<>();
        
        if (recursive) {
            Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    files.add(file);
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            try (Stream<Path> paths = Files.list(dirPath)) {
                paths.filter(Files::isRegularFile)
                     .forEach(files::add);
            }
        }
        
        return files;
    }
    
    /**
     * Find files by extension
     * @param dirPath directory to search
     * @param extension file extension (without dot)
     * @param recursive whether to search subdirectories
     * @return list of matching files
     * @throws IOException if directory cannot be read
     */
    public static List<Path> findFilesByExtension(String dirPath, String extension, boolean recursive) throws IOException {
        return findFilesByExtension(Paths.get(dirPath), extension, recursive);
    }
    
    /**
     * Find files by extension
     * @param dirPath directory to search
     * @param extension file extension (without dot)
     * @param recursive whether to search subdirectories
     * @return list of matching files
     * @throws IOException if directory cannot be read
     */
    public static List<Path> findFilesByExtension(Path dirPath, String extension, boolean recursive) throws IOException {
        if (StringUtils.isBlank(extension)) {
            return new ArrayList<>();
        }
        
        List<Path> allFiles = listFiles(dirPath, recursive);
        List<Path> matchingFiles = new ArrayList<>();
        
        String targetExtension = extension.toLowerCase();
        
        for (Path file : allFiles) {
            String fileExtension = getFileExtension(file.toString());
            if (targetExtension.equals(fileExtension)) {
                matchingFiles.add(file);
            }
        }
        
        return matchingFiles;
    }
    
    /**
     * Copy directory and all its contents
     * @param source source directory
     * @param destination destination directory
     * @throws IOException if copy fails
     */
    public static void copyDirectory(String source, String destination) throws IOException {
        copyDirectory(Paths.get(source), Paths.get(destination));
    }
    
    /**
     * Copy directory and all its contents
     * @param source source directory
     * @param destination destination directory
     * @throws IOException if copy fails
     */
    public static void copyDirectory(Path source, Path destination) throws IOException {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Source and destination paths cannot be null");
        }
        
        if (!Files.exists(source)) {
            throw new FileNotFoundException("Source directory not found: " + source);
        }
        
        if (!Files.isDirectory(source)) {
            throw new IllegalArgumentException("Source is not a directory: " + source);
        }
        
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetPath = destination.resolve(source.relativize(dir));
                Files.createDirectories(targetPath);
                return FileVisitResult.CONTINUE;
            }
            
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path targetPath = destination.resolve(source.relativize(file));
                Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    
    /**
     * Create temporary file
     * @param prefix filename prefix
     * @param suffix filename suffix
     * @return path to temporary file
     * @throws IOException if creation fails
     */
    public static Path createTempFile(String prefix, String suffix) throws IOException {
        return Files.createTempFile(prefix, suffix);
    }
    
    /**
     * Create temporary directory
     * @param prefix directory name prefix
     * @return path to temporary directory
     * @throws IOException if creation fails
     */
    public static Path createTempDirectory(String prefix) throws IOException {
        return Files.createTempDirectory(prefix);
    }
    
    /**
     * Read file as byte array
     * @param filePath path to file
     * @return file content as byte array
     * @throws IOException if file cannot be read
     */
    public static byte[] readFileToByteArray(String filePath) throws IOException {
        return readFileToByteArray(Paths.get(filePath));
    }
    
    /**
     * Read file as byte array
     * @param filePath path to file
     * @return file content as byte array
     * @throws IOException if file cannot be read
     */
    public static byte[] readFileToByteArray(Path filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        
        return Files.readAllBytes(filePath);
    }
    
    /**
     * Write byte array to file
     * @param filePath path to file
     * @param data byte data to write
     * @throws IOException if file cannot be written
     */
    public static void writeByteArrayToFile(String filePath, byte[] data) throws IOException {
        writeByteArrayToFile(Paths.get(filePath), data);
    }
    
    /**
     * Write byte array to file
     * @param filePath path to file
     * @param data byte data to write
     * @throws IOException if file cannot be written
     */
    public static void writeByteArrayToFile(Path filePath, byte[] data) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        
        if (data == null) {
            data = new byte[0];
        }
        
        // Create parent directories if they don't exist
        createDirectories(filePath.getParent());
        
        Files.write(filePath, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    /**
     * Get last modified time of file
     * @param filePath path to file
     * @return last modified time in milliseconds
     * @throws IOException if file cannot be accessed
     */
    public static long getLastModifiedTime(String filePath) throws IOException {
        return getLastModifiedTime(Paths.get(filePath));
    }
    
    /**
     * Get last modified time of file
     * @param filePath path to file
     * @return last modified time in milliseconds
     * @throws IOException if file cannot be accessed
     */
    public static long getLastModifiedTime(Path filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        
        return Files.getLastModifiedTime(filePath).toMillis();
    }
    
    /**
     * Check if file is empty
     * @param filePath path to file
     * @return true if file is empty
     * @throws IOException if file cannot be accessed
     */
    public static boolean isEmpty(String filePath) throws IOException {
        return isEmpty(Paths.get(filePath));
    }
    
    /**
     * Check if file is empty
     * @param filePath path to file
     * @return true if file is empty
     * @throws IOException if file cannot be accessed
     */
    public static boolean isEmpty(Path filePath) throws IOException {
        return getFileSize(filePath) == 0;
    }
    
    /**
     * Check if directory is empty
     * @param dirPath path to directory
     * @return true if directory is empty
     * @throws IOException if directory cannot be accessed
     */
    public static boolean isDirectoryEmpty(String dirPath) throws IOException {
        return isDirectoryEmpty(Paths.get(dirPath));
    }
    
    /**
     * Check if directory is empty
     * @param dirPath path to directory
     * @return true if directory is empty
     * @throws IOException if directory cannot be accessed
     */
    public static boolean isDirectoryEmpty(Path dirPath) throws IOException {
        if (dirPath == null) {
            throw new IllegalArgumentException("Directory path cannot be null");
        }
        
        if (!Files.exists(dirPath)) {
            throw new FileNotFoundException("Directory not found: " + dirPath);
        }
        
        if (!Files.isDirectory(dirPath)) {
            throw new IllegalArgumentException("Path is not a directory: " + dirPath);
        }
        
        try (Stream<Path> entries = Files.list(dirPath)) {
            return !entries.findFirst().isPresent();
        }
    }
}
