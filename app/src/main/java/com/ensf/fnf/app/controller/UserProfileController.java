package com.ensf.fnf.app.controller;

import com.ensf.fnf.core.dto.requestDto.UpdatePrivacyRequestDto;
import com.ensf.fnf.core.dto.requestDto.UpdateUserProfileRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.UserProfileResponseDto;
import com.ensf.fnf.core.dto.responseDto.UserProfilesResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/profile")
@CrossOrigin("*")
@Api(tags = "User Profile API")
public interface UserProfileController {

    @GetMapping("/me")
    @ApiOperation("Fetch the authenticated user's profile metadata.")
    ResponseEntity<CommonApiResponse<UserProfilesResponseDto>> getMyProfile();

    @PutMapping("/update")
    @ApiOperation("Update profile details including personal tracking elements.")
    ResponseEntity<CommonApiResponse<UserProfileResponseDto>> updateProfile(@RequestBody UpdateUserProfileRequestDto dto);

    @PutMapping("/privacy")
    @ApiOperation("Update visibility and privacy configurations")
   ResponseEntity<CommonApiResponse<String>> updatePrivacySettings(@RequestBody UpdatePrivacyRequestDto dto);
}