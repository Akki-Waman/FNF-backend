package com.sipl.ticket.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class TicketFileUploadUtil {

    @Value("${TIcketFilePath}")
    private String filePath;

    private static final String DATE_FORMAT_PATTERN = "yyyy_MM_dd_HHmmss";

    public String saveFile(
            MultipartFile file,
            Long uniqueId,
            String baseFileName,
            String moduleName) throws IOException {

        validateInputs(file, uniqueId, baseFileName);

        // 1️⃣ Ensure base path exists
        File baseDir = new File(filePath);
        if (!baseDir.exists() && !baseDir.mkdirs()) {
            throw new IOException("Failed to create base upload directory: " + filePath);
        }

        // 2️⃣ Sanitize filename (VERY IMPORTANT)
        String safeBaseFileName = baseFileName
                .replaceAll("[^a-zA-Z0-9-_]", "_")
                .replaceAll("_+", "_");

        // 3️⃣ Extract extension safely
        String originalFileName = file.getOriginalFilename();
        String extension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        log.info("Uploading file: {}, size: {}", originalFileName, file.getSize());

        // 4️⃣ Create module/ID folder
        String folderPath = createUserFolder(uniqueId, moduleName);

        // 5️⃣ Generate unique filename
        String destFileName = generateUniqueFilename(safeBaseFileName, extension, folderPath);

        // 6️⃣ Save file
        File destFile = new File(folderPath, destFileName);
        file.transferTo(destFile);

        log.info("File saved successfully at {}", destFile.getAbsolutePath());

        // 7️⃣ Return relative path
        return moduleName + "/" + uniqueId + "/" + destFileName;
    }

    private void validateInputs(MultipartFile file, Long uniqueId, String fileName) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty.");
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
