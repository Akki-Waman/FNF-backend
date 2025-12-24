package com.sipl.ticket.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
public class ApkUtil {

    @Value("${filePath}")
    private String basePath;

    public Resource downloadFile(String fileName) {
        File dir = new File(basePath + fileName);
        try {
            if (dir.exists()) {
                return new UrlResource(dir.toURI());
            }
        } catch (Exception e) {
            log.error("Exception in downloadFile ", e);
            return null;
        }
        return null;
    }
}

