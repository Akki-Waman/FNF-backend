package com.sipl.ticket.android.service.impl;

import com.sipl.ticket.android.service.AndroidVersionService;
import com.sipl.ticket.core.dao.entity.AndroidVersionMaster;
import com.sipl.ticket.core.dao.repository.AndroidVersionMasterRepository;
import com.sipl.ticket.core.dto.response.AndroidApiResponseDTO;
import com.sipl.ticket.core.util.ApkUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AndroidVersionServiceImpl implements AndroidVersionService {

    private final AndroidVersionMasterRepository versionMasterRepository;

    @Value("${filePath}")
    private String basePath;

    private final ApkUtil apkUtil;

    @Override
    public AndroidApiResponseDTO checkVersion(String appId, String version) {
        log.info("Checking Android app version for appId: {} and version: {}", appId, version);

        try {
            Optional<AndroidVersionMaster> appIdCheck = versionMasterRepository.findByAppId(appId);
            if (!appIdCheck.isPresent()) {
                log.warn("App ID not found for appId: {}", appId);
                return new AndroidApiResponseDTO(
                        false,
                        null,
                        null,
                        HttpStatus.BAD_REQUEST,
                        "Something went wrong! Unable to identify android apk. Please reach out to customer support.");
            }

            Optional<AndroidVersionMaster> androidVersionOpt =
                    versionMasterRepository.findByAppIdAndVersion(appId, version);

            if (androidVersionOpt.isPresent()) {
                log.info("App version {} is up to date for appId: {}", version, appId);
                return new AndroidApiResponseDTO(
                        false,
                        null,
                        null,
                        HttpStatus.OK,
                        "Version is up to date.");
            }

            AndroidVersionMaster androidVersionMaster = appIdCheck.get();
            log.info("A new version {} is available for appId: {}", androidVersionMaster.getVersion(), appId);

            return new AndroidApiResponseDTO(
                    true,
                    androidVersionMaster.getUrl() != null ? androidVersionMaster.getUrl().trim() : null,
                    androidVersionMaster.getVersion(),
                    HttpStatus.BAD_REQUEST,
                    "Latest version " + androidVersionMaster.getVersion()
                            + " is now available. Please install the latest version.");

        } catch (Exception e) {
            log.error("Error occurred while checking app version for appId: {} and version: {}. Exception: {}",
                    appId, version, e.getMessage(), e);
            return new AndroidApiResponseDTO(
                    false,
                    null,
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred while checking app version. Please try again later.");
        }
    }



    @Override
    public ResponseEntity<Resource> downloadFile(String fileName) {
        log.info("<<START>> downloadFile <<START>>");
        Resource file = apkUtil.downloadFile(fileName);
        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(file);
        }
    }
}
