package com.ensf.fnf.app.service;

import com.ensf.fnf.core.dto.requestDto.UpdateUserProfileRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.UserProfileResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface UserProfileService {
    CommonApiResponse<UserProfileResponseDto> getUserProfile();
    CommonApiResponse<UserProfileResponseDto> updateUserProfile(UpdateUserProfileRequestDto dto);
}
