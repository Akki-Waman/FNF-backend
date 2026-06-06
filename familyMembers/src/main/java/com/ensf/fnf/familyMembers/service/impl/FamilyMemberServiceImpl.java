package com.ensf.fnf.familyMembers.service.impl;

import com.ensf.fnf.core.dao.entity.FamilyMemberEntity;
import com.ensf.fnf.core.dao.entity.UserEntity;
import com.ensf.fnf.core.dao.repository.FamilyMemberRepository;
import com.ensf.fnf.core.dao.repository.UserRepository;
import com.ensf.fnf.core.dto.requestDto.AddFamilyMemberRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.FamilyMemberResponseDto;
import com.ensf.fnf.familyMembers.service.FamilyMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FamilyMemberServiceImpl implements FamilyMemberService {

    private final FamilyMemberRepository familyMemberRepository;
    private final UserRepository userRepository;

    @Override
    public CommonApiResponse<FamilyMemberResponseDto> addFamilyMember(AddFamilyMemberRequestDto dto) {
        log.info("<<START>> addFamilyMember service <<START>>");

        UserEntity currentUser = getCurrentUser();

        FamilyMemberEntity entity = FamilyMemberEntity.builder()
                .fullName(dto.getFullName())
                .relationshipType(dto.getRelationshipType())
                .gender(dto.getGender())
                .birthDate(dto.getBirthDate())
                .anniversaryDate(dto.getAnniversaryDate())
                .married(dto.getMarried())
                .spouseName(dto.getSpouseName())
                .spouseBirthDate(dto.getSpouseBirthDate())
                .spouseAnniversaryDate(dto.getSpouseAnniversaryDate())
                .mobileNumber(dto.getMobileNumber())
                .email(dto.getEmail())
                .active(true)
                .user(currentUser)
                .build();

        FamilyMemberEntity saved = familyMemberRepository.save(entity);

        FamilyMemberResponseDto responseDto = mapToResponseDto(saved);

        log.info("<<END>> addFamilyMember service <<END>>");

        return CommonApiResponse.<FamilyMemberResponseDto>builder()
                .success(true)
                .message("Family member added successfully")
                .data(responseDto)
                .build();
    }

    @Override
    public CommonApiResponse<List<FamilyMemberResponseDto>> getFamilyMembers() {
        log.info("<<START>> getFamilyMembers service <<START>>");

        UserEntity currentUser = getCurrentUser();

        List<FamilyMemberEntity> familyMembers = familyMemberRepository.findByUserId(currentUser.getId());

        List<FamilyMemberResponseDto> responseDtos = familyMembers.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());

        log.info("<<END>> getFamilyMembers service <<END>>");

        return CommonApiResponse.<List<FamilyMemberResponseDto>>builder()
                .success(true)
                .message("Family members retrieved successfully")
                .data(responseDtos)
                .build();
    }

    @Override
    public CommonApiResponse<FamilyMemberResponseDto> updateFamilyMember(Long id, AddFamilyMemberRequestDto dto) {
        log.info("<<START>> updateFamilyMember service <<START>>");

        UserEntity currentUser = getCurrentUser();

        FamilyMemberEntity entity = familyMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Family member not found"));

        if (!entity.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to update this family member");
        }

        entity.setFullName(dto.getFullName());
        entity.setRelationshipType(dto.getRelationshipType());
        entity.setGender(dto.getGender());
        entity.setBirthDate(dto.getBirthDate());
        entity.setAnniversaryDate(dto.getAnniversaryDate());
        entity.setMarried(dto.getMarried());
        entity.setSpouseName(dto.getSpouseName());
        entity.setSpouseBirthDate(dto.getSpouseBirthDate());
        entity.setSpouseAnniversaryDate(dto.getSpouseAnniversaryDate());
        entity.setMobileNumber(dto.getMobileNumber());
        entity.setEmail(dto.getEmail());

        FamilyMemberEntity saved = familyMemberRepository.save(entity);

        FamilyMemberResponseDto responseDto = mapToResponseDto(saved);

        log.info("<<END>> updateFamilyMember service <<END>>");

        return CommonApiResponse.<FamilyMemberResponseDto>builder()
                .success(true)
                .message("Family member updated successfully")
                .data(responseDto)
                .build();
    }

    @Override
    public CommonApiResponse<String> deleteFamilyMember(Long id) {
        log.info("<<START>> deleteFamilyMember service <<START>>");

        UserEntity currentUser = getCurrentUser();

        FamilyMemberEntity entity = familyMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Family member not found"));

        if (!entity.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to delete this family member");
        }

        entity.setActive(false);
        familyMemberRepository.save(entity);

        log.info("<<END>> deleteFamilyMember service <<END>>");

        return CommonApiResponse.<String>builder()
                .success(true)
                .message("Family member deleted successfully")
                .data(null)
                .build();
    }

    private UserEntity getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByEmail(username)
                .orElseGet(() -> userRepository.findByMobileNumber(username)
                        .orElseThrow(() -> new RuntimeException("User not found: " + username)));
    }

    private FamilyMemberResponseDto mapToResponseDto(FamilyMemberEntity entity) {
        return FamilyMemberResponseDto.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .relationshipType(entity.getRelationshipType())
                .gender(entity.getGender())
                .birthDate(entity.getBirthDate())
                .anniversaryDate(entity.getAnniversaryDate())
                .married(entity.getMarried())
                .spouseName(entity.getSpouseName())
                .spouseBirthDate(entity.getSpouseBirthDate())
                .spouseAnniversaryDate(entity.getSpouseAnniversaryDate())
                .mobileNumber(entity.getMobileNumber())
                .email(entity.getEmail())
                .active(entity.getActive())
                .build();
    }
}
