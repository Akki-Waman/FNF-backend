package com.ensf.fnf.app.service;

import com.ensf.fnf.core.dto.requestDto.UpdatePrivacyRequestDto;
import com.ensf.fnf.core.dto.requestDto.UpdateUserProfileRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.UserProfileResponseDto;
import com.ensf.fnf.core.dto.responseDto.UserProfilesResponseDto;

public interface UserProfileService {
    CommonApiResponse<UserProfilesResponseDto> getCurrentUserProfile();
    CommonApiResponse<UserProfileResponseDto> updateCurrentUserProfile(UpdateUserProfileRequestDto dto);
    CommonApiResponse<String> updatePrivacy(UpdatePrivacyRequestDto dto);
}