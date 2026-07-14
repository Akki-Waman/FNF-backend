package com.ensf.fnf.family.service.impl;

import com.ensf.fnf.core.dao.entity.FamilyMemberEntity;
import com.ensf.fnf.core.dao.entity.UserEntity;
import com.ensf.fnf.core.dao.repository.FamilyMemberRepository;
import com.ensf.fnf.core.dao.repository.UserRepository;
import com.ensf.fnf.core.dto.requestDto.CreateFamilyMemberRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.FamilyMemberResponseDto;
import com.ensf.fnf.core.dto.responseDto.HomeDashboardResponseDto;
import com.ensf.fnf.core.mapper.FamilyMemberMapper;
import com.ensf.fnf.family.service.FamilyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FamilyServiceImpl implements FamilyService {

    private final UserRepository userRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final FamilyMemberMapper familyMemberMapper;

    @Override
    @Transactional(readOnly = true)
    public CommonApiResponse<HomeDashboardResponseDto> fetchUserFamilyTree() {
        UserEntity user = getAuthenticatedUser();
        List<FamilyMemberEntity> members = familyMemberRepository.findByUserId(user.getUserId());

        HomeDashboardResponseDto dashboard = HomeDashboardResponseDto.builder()
                .userId(user.getUserId())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmailAddress())
                .mobileNumber(user.getMobileNumber())
                .familyMembers(familyMemberMapper.toDtoList(members))
                .build();

        return CommonApiResponse.<HomeDashboardResponseDto>builder()
                .success(true)
                .message("Family structural tree loaded.")
                .data(dashboard)
                .build();
    }

    @Override
    public CommonApiResponse<FamilyMemberResponseDto> createGraphNode(CreateFamilyMemberRequestDto dto) {
        UserEntity user = getAuthenticatedUser();

        FamilyMemberEntity entity = familyMemberMapper.toEntity(dto);
        entity.setUser(user); // Mapping relation baseline owner contextual link

        if (dto.getParentMemberId() != null) {
            FamilyMemberEntity parent = familyMemberRepository.findById(dto.getParentMemberId())
                    .orElseThrow(() -> new RuntimeException("Specified parent coordinate element node target not found."));
            entity.setParentMember(parent);
        }

        FamilyMemberEntity saved = familyMemberRepository.save(entity);
        log.info("Appended new structural tree relationship mapping id: {}", saved.getFamilyMemberId());

        return CommonApiResponse.<FamilyMemberResponseDto>builder()
                .success(true)
                .message("Dynamic graph node successfully registered.")
                .data(familyMemberMapper.toDto(saved))
                .build();
    }

    private UserEntity getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Identity validation mapping token evaluation error."));
    }

    @Override
    @Transactional
    public CommonApiResponse<String> removeFamilyNode(Long nodeId) {
        UserEntity user = getAuthenticatedUser();
        log.info("Attempting to delete family node {} for user {}", nodeId, user.getUserId());

        FamilyMemberEntity node = familyMemberRepository.findById(nodeId)
                .orElseThrow(() -> new RuntimeException("Family member node not found"));

        verifyNodeOwnership(user, node);
        familyMemberRepository.delete(node);

        log.info("Successfully deleted node: {}", nodeId);
        return CommonApiResponse.<String>builder().success(true).message("Relative removed from tree").build();
    }

    private void verifyNodeOwnership(UserEntity user, FamilyMemberEntity node) {
        if (!node.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("Unauthorized to modify this tree");
        }
    }
}