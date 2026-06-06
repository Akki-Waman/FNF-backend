package com.ensf.fnf.app.controller;

import com.ensf.fnf.core.dto.requestDto.UpdateUserProfileRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.UserProfileResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/profile")
@CrossOrigin("*")
@Api(tags = "User Profile API")
public interface UserProfileController {

    @ApiOperation(value = "Get User Profile")
    @GetMapping
    ResponseEntity<CommonApiResponse<UserProfileResponseDto>> getUserProfile();

    @ApiOperation(value = "Update User Profile")
    @PutMapping
    ResponseEntity<CommonApiResponse<UserProfileResponseDto>> updateUserProfile(
            @RequestBody UpdateUserProfileRequestDto dto
    );
}
