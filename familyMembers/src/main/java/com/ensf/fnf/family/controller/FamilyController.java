package com.ensf.fnf.family.controller;

import com.ensf.fnf.core.dto.requestDto.CreateFamilyTreeRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.FamilyMemberResponseDto;
import com.ensf.fnf.core.dto.responseDto.HomeDashboardResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/family")
@Api(tags = "Family Member APIs")
public interface FamilyController {


}