package com.eduplatform.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FileUtils {

    private static final List<String> ALLOWED_VIDEO_TYPES = Arrays.asList("video/mp4", "video/webm");
    private static final List<String> ALLOWED_DOCUMENT_TYPES = Arrays.asList("application/pdf", "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
    private static final List<String> ALLOWED_ASSIGNMENT_TYPES = Arrays.asList("application/pdf", "application/zip", "text/plain");

    public static boolean isValidVideoFile(MultipartFile file) {
        return ALLOWED_VIDEO_TYPES.contains(file.getContentType());
    }

    public static boolean isValidDocumentFile(MultipartFile file) {
        return ALLOWED_DOCUMENT_TYPES.contains(file.getContentType());
    }

    public static boolean isValidAssignmentFile(MultipartFile file) {
        return ALLOWED_ASSIGNMENT_TYPES.contains(file.getContentType());
    }

    public static String saveFile(MultipartFile file, String directory) throws IOException {
        byte[] bytes = file.getBytes();
        Path path = Paths.get(directory + File.separator + file.getOriginalFilename());
        Files.createDirectories(path.getParent());
        Files.write(path, bytes);
        return path.toString();
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }
}