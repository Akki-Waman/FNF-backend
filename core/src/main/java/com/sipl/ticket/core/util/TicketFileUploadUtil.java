package com.sipl.ticket.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class TicketFileUploadUtil {

    @Value("${TIcketFilePath}")
    private String filePath;

    private static final String DATE_FORMAT_PATTERN = "yyyy_MM_dd_HHmmss";

    public String saveFile(
            byte[] fileBytes,
            String originalFileName,
            Long uniqueId,
            String moduleName,
            String name
    ) throws IOException {

        validateInputs(fileBytes, uniqueId, originalFileName);

        // 1️⃣ Ensure base/module/id directory exists
        String folderPath = createUserFolder(uniqueId, moduleName);

        // 2️⃣ Sanitize filename
        String baseFileName = extractBaseName(originalFileName);
        String safeBaseFileName = baseFileName
                .replaceAll("[^a-zA-Z0-9-_]", "_")
                .replaceAll("_+", "_");

        // 3️⃣ Extract extension
        String extension = extractExtension(originalFileName);

        // 4️⃣ Generate unique filename
        String destFileName =
                generateUniqueFilename(safeBaseFileName, extension, folderPath);

        // 5️⃣ Write file using bytes (ONLY ONCE)
        Path destination = Paths.get(folderPath, destFileName);
        Files.write(destination, fileBytes);

        log.info("File saved successfully at {}", destination.toAbsolutePath());

        // ✅ 6️⃣ Return ABSOLUTE path (for DMS upload)
        return destination.toAbsolutePath().toString();
    }


    private String extractBaseName(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex > 0) ? fileName.substring(0, dotIndex) : fileName;
    }

    private String extractExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex > 0) ? fileName.substring(dotIndex) : "";
    }

    private void validateInputs(byte[] fileBytes, Long uniqueId, String fileName) {
        if (fileBytes == null || fileBytes.length == 0) {
            throw new IllegalArgumentException("File bytes cannot be null or empty.");
        }
        if (uniqueId == null) {
            throw new IllegalArgumentException("Unique ID cannot be null.");
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty.");
        }
    }


    private String createUserFolder(Long uniqueId, String moduleName) throws IOException {
        String userFolderPath =
                filePath + File.separator + moduleName + File.separator + uniqueId;

        File folder = new File(userFolderPath);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Failed to create directory: " + userFolderPath);
        }
        return userFolderPath;
    }

    private String generateUniqueFilename(
            String baseFileName, String extension, String folderPath) {

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));

        int counter = 1;
        String fileName;

        do {
            fileName = baseFileName + "_" + timestamp + "_" + counter++ + extension;
        } while (new File(folderPath, fileName).exists());

        return fileName;
    }
}
