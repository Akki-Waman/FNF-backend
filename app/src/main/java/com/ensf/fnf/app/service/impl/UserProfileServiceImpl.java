package com.ensf.fnf.app.service.impl;

import com.ensf.fnf.app.service.UserProfileService;
import com.ensf.fnf.core.dao.entity.UserEntity;
import com.ensf.fnf.core.dao.repository.UserRepository;
import com.ensf.fnf.core.dto.requestDto.UpdateUserProfileRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.UserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;

    @Override
    public CommonApiResponse<UserProfileResponseDto> getUserProfile() {
        UserEntity currentUser = getCurrentUser();
        return CommonApiResponse.<UserProfileResponseDto>builder()
                .success(true)
                .message("Profile retrieved successfully")
                .data(mapToDto(currentUser))
                .build();
    }

    @Override
    public CommonApiResponse<UserProfileResponseDto> updateUserProfile(UpdateUserProfileRequestDto dto) {
        UserEntity currentUser = getCurrentUser();

        if (dto.getFullName() != null) {
            currentUser.setFullName(dto.getFullName());
        }
        if (dto.getGender() != null) {
            currentUser.setGender(dto.getGender());
        }
        if (dto.getDateOfBirth() != null) {
            currentUser.setDateOfBirth(dto.getDateOfBirth());
        }
        if (dto.getMarried() != null) {
            currentUser.setMarried(dto.getMarried());
        }
        if (dto.getSpouseName() != null) {
            currentUser.setSpouseName(dto.getSpouseName());
        }
        if (dto.getSpouseDob() != null) {
            currentUser.setSpouseDob(dto.getSpouseDob());
        }
        if (dto.getAnniversaryDate() != null) {
            currentUser.setAnniversaryDate(dto.getAnniversaryDate());
        }

        UserEntity saved = userRepository.save(currentUser);

        return CommonApiResponse.<UserProfileResponseDto>builder()
                .success(true)
                .message("Profile updated successfully")
                .data(mapToDto(saved))
                .build();
    }

    private UserEntity getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByEmail(username)
                .orElseGet(() -> userRepository.findByMobileNumber(username)
                        .orElseThrow(() -> new RuntimeException("User not found: " + username)));
    }

    private UserProfileResponseDto mapToDto(UserEntity user) {
        return UserProfileResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .married(user.getMarried())
                .spouseName(user.getSpouseName())
                .spouseDob(user.getSpouseDob())
                .anniversaryDate(user.getAnniversaryDate())
                .profileComplete(user.isProfileComplete())
                .build();
    }
}
