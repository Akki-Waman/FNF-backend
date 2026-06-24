package com.ensf.fnf.app.controller.impl;

import com.ensf.fnf.app.controller.UserProfileController;
import com.ensf.fnf.app.service.UserProfileService;
import com.ensf.fnf.core.dto.requestDto.UpdateUserProfileRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.UserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserProfileControllerImpl implements UserProfileController {

    private final UserProfileService userProfileService;


}
