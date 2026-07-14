package com.ensf.fnf.vault.service;

import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.CursorPageResponse;
import com.ensf.fnf.core.dto.responseDto.MemoryResponseDto;

import java.util.Map;

public interface VaultService {
    CommonApiResponse<Map<String, String>> generateUploadIntent(String assetExtension);
    CommonApiResponse<String> saveMemoryRecord(String cloudAssetUrl, String mediaType, String caption, String albumName);
    CommonApiResponse<CursorPageResponse<MemoryResponseDto>> fetchUserMemories(String mediaType, Long cursorId, int limit);}