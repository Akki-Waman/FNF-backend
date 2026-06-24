package com.ensf.fnf.familyMembers.controller.impl;

import com.ensf.fnf.core.dto.requestDto.AddFamilyMemberRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.FamilyMemberResponseDto;
import com.ensf.fnf.familyMembers.controller.FamilyMemberController;
import com.ensf.fnf.familyMembers.service.FamilyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FamilyMemberControllerImpl implements FamilyMemberController {

    private final FamilyMemberService familyMemberService;

}
