package com.ensf.fnf.vault.controller;

import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.CursorPageResponse;
import com.ensf.fnf.core.dto.responseDto.MemoryResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@RequestMapping("/api/v1/vault")
@Api(tags = "Digital Vault & Memories API")
public interface VaultController {

    @GetMapping("/intent")
    @ApiOperation("Generate a secure pre-signed cloud URL for direct media upload")
    ResponseEntity<CommonApiResponse<Map<String, String>>> requestUploadIntent(@RequestParam String assetExtension);

    @PostMapping("/commit")
    @ApiOperation("Save uploaded media asset metadata to the database")
    ResponseEntity<CommonApiResponse<String>> commitMemory(
            @RequestParam String cloudAssetUrl,
            @RequestParam String mediaType,
            @RequestParam(required = false) String caption,
            @RequestParam(required = false) String albumName);

    @GetMapping("/list")
    @ApiOperation("Fetch paginated memories using cursor for high performance")
    ResponseEntity<CommonApiResponse<CursorPageResponse<MemoryResponseDto>>> getMemories(
            @RequestParam(required = false) String mediaType,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "20") int limit);
}