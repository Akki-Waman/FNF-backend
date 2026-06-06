package com.ensf.fnf.family.service;

import com.ensf.fnf.core.dto.requestDto.CreateFamilyTreeRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.FamilyMemberResponseDto;
import com.ensf.fnf.core.dto.responseDto.HomeDashboardResponseDto;

import java.util.List;

public interface FamilyMemberService {

    CommonApiResponse<String> createFamilyTree(
            CreateFamilyTreeRequestDto dto
    );

    CommonApiResponse<List<FamilyMemberResponseDto>> getFamilyMembers();

    CommonApiResponse<HomeDashboardResponseDto> getDashboard();
}