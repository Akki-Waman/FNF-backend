package com.ensf.fnf.app.controller.impl;

import com.ensf.fnf.app.controller.UserProfileController;
import com.ensf.fnf.app.service.UserProfileService;
import com.ensf.fnf.core.dto.requestDto.UpdatePrivacyRequestDto;
import com.ensf.fnf.core.dto.requestDto.UpdateUserProfileRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.UserProfileResponseDto;
import com.ensf.fnf.core.dto.responseDto.UserProfilesResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserProfileControllerImpl implements UserProfileController {

    private final UserProfileService userProfileService;

    @Override
    public ResponseEntity<CommonApiResponse<UserProfilesResponseDto>> getMyProfile() {
        log.info("<<START>> UserProfileControllerImpl :: getMyProfile <<START>>");
        CommonApiResponse<UserProfilesResponseDto> response = userProfileService.getCurrentUserProfile();
        log.info("<<END>> UserProfileControllerImpl :: getMyProfile <<END>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommonApiResponse<UserProfileResponseDto>> updateProfile(UpdateUserProfileRequestDto dto) {
        return ResponseEntity.ok(userProfileService.updateCurrentUserProfile(dto));
    }

    @Override
    public ResponseEntity<CommonApiResponse<String>> updatePrivacySettings(UpdatePrivacyRequestDto dto) {
        log.info("<<START>> UserProfileControllerImpl :: updatePrivacySettings <<START>>");
       CommonApiResponse<String> response = userProfileService.updatePrivacy(dto);
        log.info("<<END>> UserProfileControllerImpl :: updatePrivacySettings <<END>>");
        return ResponseEntity.ok(response);
    }
}