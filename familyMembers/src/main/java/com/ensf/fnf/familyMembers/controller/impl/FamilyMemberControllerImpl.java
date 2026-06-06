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

    @Override
    public ResponseEntity<CommonApiResponse<FamilyMemberResponseDto>> addFamilyMember(AddFamilyMemberRequestDto dto) {
        return ResponseEntity.ok(familyMemberService.addFamilyMember(dto));
    }

    @Override
    public ResponseEntity<CommonApiResponse<List<FamilyMemberResponseDto>>> getFamilyMembers() {
        return ResponseEntity.ok(familyMemberService.getFamilyMembers());
    }

    @Override
    public ResponseEntity<CommonApiResponse<FamilyMemberResponseDto>> updateFamilyMember(Long id, AddFamilyMemberRequestDto dto) {
        return ResponseEntity.ok(familyMemberService.updateFamilyMember(id, dto));
    }

    @Override
    public ResponseEntity<CommonApiResponse<String>> deleteFamilyMember(Long id) {
        return ResponseEntity.ok(familyMemberService.deleteFamilyMember(id));
    }
}
