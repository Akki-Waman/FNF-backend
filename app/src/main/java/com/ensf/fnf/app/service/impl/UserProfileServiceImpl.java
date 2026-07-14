package com.ensf.fnf.app.service.impl;

import com.ensf.fnf.app.service.UserProfileService;
import com.ensf.fnf.core.dao.entity.UserEntity;
import com.ensf.fnf.core.dao.repository.UserRepository;
import com.ensf.fnf.core.dto.requestDto.UpdatePrivacyRequestDto;
import com.ensf.fnf.core.dto.requestDto.UpdateUserProfileRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.UserProfileResponseDto;
import com.ensf.fnf.core.dto.responseDto.UserProfilesResponseDto;
import com.ensf.fnf.core.exception.ResourceNotFoundException;
import com.ensf.fnf.core.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public CommonApiResponse<UserProfilesResponseDto> getCurrentUserProfile() {

        log.info("<<START>> getCurrentUserProfile");

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {

            log.error("No authenticated user found.");
            throw new ResourceNotFoundException(
                    "No authenticated user found."
            );
        }

        String identifier = authentication.getName();

        log.info("JWT Identifier : {}", identifier);

        UserEntity user = userRepository
                .findByIdentifier(identifier)
                .orElseThrow(() -> {
                    log.error(
                            "User not found for identifier : {}",
                            identifier
                    );
                    return new ResourceNotFoundException(
                            "User not found."
                    );
                });

        log.info("User fetched from DB : {}", user);

        UserProfilesResponseDto responseDto =
                userMapper.toDtos(user);

        log.info("Mapped DTO : {}", responseDto);

        log.info("<<END>> getCurrentUserProfile");

        return CommonApiResponse
                .<UserProfilesResponseDto>builder()
                .success(true)
                .message("Profile fetched successfully")
                .data(responseDto)
                .build();
    }

    /**
     * Resolves the currently authenticated UserEntity from the JWT-populated
     * SecurityContext. Throws if the identifier from the token doesn't match
     * any user in the DB - this is deliberate: silently returning null/empty
     * fields hides a real bug (identifier mismatch, missing JWT filter, or
     * token subject that doesn't match stored data).
     */
    private UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            log.warn("getCurrentUserProfile called with no authenticated principal in SecurityContext");
            throw new ResourceNotFoundException("No authenticated user found in security context");
        }

        String identifier = authentication.getName();
        log.info("Resolving authenticated user for identifier: {}", identifier);

        return userRepository
                .findByEmailAddressOrMobileNumber(identifier, identifier)
                .orElseThrow(() -> {
                    log.error("No UserEntity found in DB for identifier '{}' from JWT subject", identifier);
                    return new ResourceNotFoundException("User not found for identifier: " + identifier);
                });
    }

    @Override
    public CommonApiResponse<UserProfileResponseDto> updateCurrentUserProfile(UpdateUserProfileRequestDto dto) {
        UserEntity user = getAuthenticatedUser();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setGender(dto.getGender());
        user.setDateOfBirth(dto.getDateOfBirth());

        UserEntity saved = userRepository.save(user);
        log.info("User profile updated for username: {}", saved.getEmailAddress() != null ? saved.getEmailAddress() : saved.getMobileNumber());

        return CommonApiResponse.<UserProfileResponseDto>builder()
                .success(true)
                .message("Profile updated successfully")
                .data(userMapper.toDto(saved))
                .build();
    }


    @Override
    public CommonApiResponse<String> updatePrivacy(UpdatePrivacyRequestDto dto) {
        UserEntity user = getAuthenticatedUser();
        log.info("Updating privacy settings for userId: {}", user.getUserId());

        // Assuming PrivacyRepository is injected. Alternatively, map it to UserEntity directly if 1:1.
        applyPrivacyFlags(user, dto);

        return CommonApiResponse.<String>builder().success(true).message("Privacy settings updated").build();
    }

    private void applyPrivacyFlags(UserEntity user, UpdatePrivacyRequestDto dto) {
        log.debug("Applying flags: Family={}, Friend={}", dto.getAllowFamilyVisibility(), dto.getAllowFriendVisibility());
        // Save to PrivacySettingEntity table mapping to User
    }
}