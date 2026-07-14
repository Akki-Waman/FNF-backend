package com.ensf.fnf.family.service;

import com.ensf.fnf.core.dto.requestDto.CreateFamilyMemberRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.FamilyMemberResponseDto;
import com.ensf.fnf.core.dto.responseDto.HomeDashboardResponseDto;

public interface FamilyService {
    CommonApiResponse<HomeDashboardResponseDto> fetchUserFamilyTree();
    CommonApiResponse<FamilyMemberResponseDto> createGraphNode(CreateFamilyMemberRequestDto dto);
    CommonApiResponse<String> removeFamilyNode(Long nodeId);
}