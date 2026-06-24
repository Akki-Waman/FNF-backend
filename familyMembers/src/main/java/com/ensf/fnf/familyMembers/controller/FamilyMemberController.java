package com.ensf.fnf.familyMembers.controller;

import com.ensf.fnf.core.dto.requestDto.AddFamilyMemberRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.FamilyMemberResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/family-members")
@CrossOrigin("*")
@Api(tags = "Family Members API")
public interface FamilyMemberController {


}
