package com.sipl.ticket.core.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class FileUploadUtil {

    @Value("${BaseFilePath}")
    private String filePath;

    private static final String DATE_FORMAT_PATTERN = "yyyy_MM_dd_HHmmss";

    public String saveFile(MultipartFile file, Long uniqueId, String fileName, String moduleName)
            throws IOException {
        // Validate inputs
        validateInputs(file, uniqueId, fileName);

        // Get original filename and extension
        String originalFileName = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFileName).toLowerCase();

        log.info(
                "Saving file: {}, size: {} bytes, extension: {}",
                originalFileName,
                file.getSize(),
                extension);

        // Create user folder if it doesn't exist
        String userFolderPath = createUserFolder(uniqueId, moduleName);

        // Generate unique filename with timestamp
        String destFileName = generateUniqueFilename(fileName, extension, userFolderPath);

        // Save the file to the destination path
        File destFile = new File(userFolderPath, destFileName);
        file.transferTo(destFile);

        return moduleName + "/" + uniqueId + "/" + destFileName;
    }

    private void validateInputs(MultipartFile file, Long productId, String fileName) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty.");
        }
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null.");
        }
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty.");
        }
    }

    private String createUserFolder(Long uniqueId, String moduleName) throws IOException {
        String userFolderPath = filePath + File.separator + moduleName + File.separator + uniqueId;
        File folder = new File(userFolderPath);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Failed to create directory: " + userFolderPath);
        }
        return userFolderPath;
    }

    private String generateUniqueFilename(String baseFileName, String extension, String folderPath) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
        String timestamp = now.format(formatter);

        int fileNumber = 1;
        String destFileName;

        do {
            destFileName = baseFileName + "" + timestamp + "" + fileNumber++ + "." + extension;
            File destFile = new File(folderPath, destFileName);
            if (!destFile.exists()) {
                break;
            }
        } while (true);

        return destFileName;
    }
}
