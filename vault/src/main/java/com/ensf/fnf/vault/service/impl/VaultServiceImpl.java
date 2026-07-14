package com.ensf.fnf.vault.service.impl;

import com.ensf.fnf.core.dao.entity.MemoryEntity;
import com.ensf.fnf.core.dao.entity.UserEntity;
import com.ensf.fnf.core.dao.repository.MemoryRepository;
import com.ensf.fnf.core.dao.repository.UserRepository;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.CursorPageResponse;
import com.ensf.fnf.core.dto.responseDto.MemoryResponseDto;
import com.ensf.fnf.core.mapper.MemoryMapper;
import com.ensf.fnf.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaultServiceImpl implements VaultService {

    private final UserRepository userRepository;
    private final MemoryRepository memoryRepository;
    private final MemoryMapper memoryMapper;

    @Override
    public CommonApiResponse<Map<String, String>> generateUploadIntent(String assetExtension) {
        log.info("Generating secure cloud upload intent for extension: {}", assetExtension);

        String uniqueAssetKey = UUID.randomUUID().toString() + "." + assetExtension;
        String preSignedUrl = generateSecureCloudUrl(uniqueAssetKey);
        String finalAssetUrl = "https://cdn.fnf-app.com/vault/" + uniqueAssetKey;

        Map<String, String> urls = new HashMap<>();
        urls.put("uploadUrl", preSignedUrl);
        urls.put("assetUrl", finalAssetUrl);

        return CommonApiResponse.<Map<String, String>>builder().success(true).data(urls).build();
    }

    @Override
    @Transactional
    public CommonApiResponse<String> saveMemoryRecord(String cloudAssetUrl, String mediaType, String caption, String albumName) {
        UserEntity user = getAuthenticatedUser();
        validateMediaType(mediaType);

        persistMemoryToDb(user, cloudAssetUrl, mediaType, caption, albumName);

        return CommonApiResponse.<String>builder().success(true).message("Memory vaulted securely.").build();
    }

    @Override
    @Transactional(readOnly = true)
    public CommonApiResponse<CursorPageResponse<MemoryResponseDto>> fetchUserMemories(String mediaType, Long cursorId, int limit) {
        UserEntity user = getAuthenticatedUser();

        List<MemoryEntity> entities = memoryRepository.findUserGalleryWithCursor(
                user.getUserId(), mediaType, cursorId, PageRequest.of(0, limit + 1));

        CursorPageResponse<MemoryResponseDto> page = formatCursorResponse(entities, limit);

        return CommonApiResponse.<CursorPageResponse<MemoryResponseDto>>builder().success(true).data(page).build();
    }

    // ==========================================
    // PRIVATE HELPER METHODS
    // ==========================================

    private UserEntity getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Auth context invalid"));
    }

    private String generateSecureCloudUrl(String key) {
        log.debug("Mocking AWS S3 / GCP pre-signed URL generation for key: {}", key);
        return "https://cloud-storage.provider.com/upload/" + key + "?token=secure_hash";
    }

    private void validateMediaType(String mediaType) {
        if (!mediaType.equalsIgnoreCase("PHOTO") && !mediaType.equalsIgnoreCase("VIDEO")) {
            log.error("Invalid media type submitted: {}", mediaType);
            throw new IllegalArgumentException("Media type must be PHOTO or VIDEO");
        }
    }

    private void persistMemoryToDb(UserEntity user, String url, String type, String caption, String album) {
        log.debug("Persisting memory entity for user: {}", user.getUserId());
        MemoryEntity memory = MemoryEntity.builder()
                .user(user)
                .mediaUrl(url)
                .mediaType(type.toUpperCase())
                .caption(caption)
                .albumName(album)
                .build();
        memoryRepository.save(memory);
    }

    private CursorPageResponse<MemoryResponseDto> formatCursorResponse(List<MemoryEntity> entities, int limit) {
        boolean hasNext = entities.size() > limit;
        if (hasNext) {
            entities.remove(limit);
        }
        Long nextCursor = entities.isEmpty() ? null : entities.get(entities.size() - 1).getMemoryId();

        return CursorPageResponse.<MemoryResponseDto>builder()
                .content(memoryMapper.toDtoList(entities))
                .hasNext(hasNext)
                .nextCursorId(nextCursor)
                .build();
    }
}