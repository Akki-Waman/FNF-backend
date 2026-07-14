package com.ensf.fnf.family.controller.impl;

import com.ensf.fnf.core.dto.requestDto.CreateFamilyMemberRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.FamilyMemberResponseDto;
import com.ensf.fnf.core.dto.responseDto.HomeDashboardResponseDto;
import com.ensf.fnf.family.controller.FamilyController;
import com.ensf.fnf.family.service.FamilyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FamilyControllerImpl implements FamilyController {

    private final FamilyService familyService;

    @Override
    public ResponseEntity<CommonApiResponse<HomeDashboardResponseDto>> getFamilyTree() {
        log.info("<<START>> FamilyControllerImpl :: getFamilyTree <<START>>");
        CommonApiResponse<HomeDashboardResponseDto> response = familyService.fetchUserFamilyTree();
        log.info("<<END>> FamilyControllerImpl :: getFamilyTree <<END>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommonApiResponse<FamilyMemberResponseDto>> addFamilyMemberNode(CreateFamilyMemberRequestDto dto) {
        log.info("<<START>> FamilyControllerImpl :: addFamilyMemberNode <<START>>");
        CommonApiResponse<FamilyMemberResponseDto> response = familyService.createGraphNode(dto);
        log.info("<<END>> FamilyControllerImpl :: addFamilyMemberNode <<END>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommonApiResponse<String>> removeFamilyNode(Long nodeId) {
        log.info("<<START>> FamilyControllerImpl :: removeFamilyNode <<START>>");
        CommonApiResponse<String> response = familyService.removeFamilyNode(nodeId);
        log.info("<<END>> FamilyControllerImpl :: removeFamilyNode <<END>>");
        return ResponseEntity.ok(response);
    }


}