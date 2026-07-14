package com.ensf.fnf.vault.controller.impl;

import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.CursorPageResponse;
import com.ensf.fnf.core.dto.responseDto.MemoryResponseDto;
import com.ensf.fnf.vault.controller.VaultController;
import com.ensf.fnf.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VaultControllerImpl implements VaultController {

    private final VaultService vaultService;

    @Override
    public ResponseEntity<CommonApiResponse<Map<String, String>>> requestUploadIntent(String assetExtension) {
        log.info("<<START>> VaultControllerImpl :: requestUploadIntent <<START>>");
        CommonApiResponse<Map<String, String>> response = vaultService.generateUploadIntent(assetExtension);
        log.info("<<END>> VaultControllerImpl :: requestUploadIntent <<END>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommonApiResponse<String>> commitMemory(String cloudAssetUrl, String mediaType, String caption, String albumName) {
        log.info("<<START>> VaultControllerImpl :: commitMemory <<START>>");
        CommonApiResponse<String> response = vaultService.saveMemoryRecord(cloudAssetUrl, mediaType, caption, albumName);
        log.info("<<END>> VaultControllerImpl :: commitMemory <<END>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommonApiResponse<CursorPageResponse<MemoryResponseDto>>> getMemories(String mediaType, Long cursorId, int limit) {
        log.info("<<START>> VaultControllerImpl :: getMemories <<START>>");
        CommonApiResponse<CursorPageResponse<MemoryResponseDto>> response = vaultService.fetchUserMemories(mediaType, cursorId, limit);
        log.info("<<END>> VaultControllerImpl :: getMemories <<END>>");
        return ResponseEntity.ok(response);
    }
}