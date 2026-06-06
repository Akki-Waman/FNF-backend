package com.ensf.fnf.familyMembers.service;

import com.ensf.fnf.core.dto.requestDto.AddFamilyMemberRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.FamilyMemberResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FamilyMemberService {

    CommonApiResponse<FamilyMemberResponseDto> addFamilyMember(AddFamilyMemberRequestDto dto);

    CommonApiResponse<List<FamilyMemberResponseDto>> getFamilyMembers();

    CommonApiResponse<FamilyMemberResponseDto> updateFamilyMember(Long id, AddFamilyMemberRequestDto dto);

    CommonApiResponse<String> deleteFamilyMember(Long id);
}
