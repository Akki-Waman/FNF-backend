package com.ensf.fnf.family.controller.impl;

import com.ensf.fnf.core.dto.requestDto.CreateFamilyTreeRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.FamilyMemberResponseDto;
import com.ensf.fnf.core.dto.responseDto.HomeDashboardResponseDto;
import com.ensf.fnf.family.controller.FamilyMemberController;
import com.ensf.fnf.family.service.FamilyMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FamilyMemberControllerImpl
        implements FamilyMemberController {

    private final FamilyMemberService familyMemberService;

    @Override
    public ResponseEntity<CommonApiResponse<String>> createFamilyTree(
            CreateFamilyTreeRequestDto dto) {

        log.info("<<START>> createFamilyTree API <<START>>");

        return ResponseEntity.ok(
                familyMemberService.createFamilyTree(dto)
        );
    }

    @Override
    public ResponseEntity<CommonApiResponse<List<FamilyMemberResponseDto>>> getFamilyMembers() {

        log.info("<<START>> getFamilyMembers API <<START>>");

        return ResponseEntity.ok(
                familyMemberService.getFamilyMembers()
        );
    }

    @Override
    public ResponseEntity<CommonApiResponse<HomeDashboardResponseDto>> getDashboard() {

        log.info("<<START>> getDashboard API <<START>>");

        return ResponseEntity.ok(
                familyMemberService.getDashboard()
        );
    }
}