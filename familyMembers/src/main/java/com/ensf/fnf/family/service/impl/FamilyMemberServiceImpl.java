package com.ensf.fnf.family.service.impl;

import com.ensf.fnf.core.dao.entity.FamilyMemberEntity;
import com.ensf.fnf.core.dao.entity.UserEntity;
import com.ensf.fnf.core.dao.repository.FamilyMemberRepository;
import com.ensf.fnf.core.dao.repository.UserRepository;
import com.ensf.fnf.core.dto.requestDto.CreateFamilyMemberRequestDto;
import com.ensf.fnf.core.dto.requestDto.CreateFamilyTreeRequestDto;
import com.ensf.fnf.core.dto.responseDto.CommonApiResponse;
import com.ensf.fnf.core.dto.responseDto.FamilyMemberResponseDto;
import com.ensf.fnf.core.dto.responseDto.HomeDashboardResponseDto;
import com.ensf.fnf.core.mapper.FamilyMemberMapper;
import com.ensf.fnf.family.service.FamilyMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FamilyMemberServiceImpl
        implements FamilyMemberService {

    private final UserRepository userRepository;

    private final FamilyMemberRepository familyMemberRepository;

    private final FamilyMemberMapper familyMemberMapper;

    @Override
    public CommonApiResponse<String> createFamilyTree(
            CreateFamilyTreeRequestDto dto) {

        log.info(
                "<<START>> createFamilyTree <<START>>"
        );

        UserEntity user =
                getLoggedInUser();

        validateRequest(
                dto
        );

        saveFamilyMembers(
                dto,
                user
        );

        log.info(
                "<<END>> createFamilyTree <<END>>"
        );

        return CommonApiResponse
                .<String>builder()
                .success(true)
                .message(
                        "Family tree saved successfully"
                )
                .build();
    }

    @Override
    public CommonApiResponse<
            List<FamilyMemberResponseDto>>
    getFamilyMembers() {

        log.info(
                "<<START>> getFamilyMembers <<START>>"
        );

        UserEntity user =
                getLoggedInUser();

        List<FamilyMemberEntity> entities =
                familyMemberRepository
                        .findByUserId(
                                user.getId()
                        );

        List<FamilyMemberResponseDto> response =
                familyMemberMapper
                        .toDtoList(
                                entities
                        );

        log.info(
                "<<END>> getFamilyMembers <<END>>"
        );

        return CommonApiResponse
                .<List<FamilyMemberResponseDto>>
                        builder()
                .success(true)
                .message(
                        "Family members fetched successfully"
                )
                .data(
                        response
                )
                .build();
    }

    @Override
    public CommonApiResponse<
            HomeDashboardResponseDto>
    getDashboard() {

        log.info(
                "<<START>> getDashboard <<START>>"
        );

        UserEntity user =
                getLoggedInUser();

        HomeDashboardResponseDto response =
                buildDashboardResponse(
                        user
                );

        log.info(
                "<<END>> getDashboard <<END>>"
        );

        return CommonApiResponse
                .<HomeDashboardResponseDto>
                        builder()
                .success(true)
                .message(
                        "Dashboard fetched successfully"
                )
                .data(
                        response
                )
                .build();
    }

    private UserEntity getLoggedInUser() {

        log.info(
                "<<START>> getLoggedInUser <<START>>"
        );

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        UserEntity user =
                userRepository
                        .findByEmail(
                                email
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "User not found"
                                        )
                        );

        log.info(
                "<<END>> getLoggedInUser <<END>>"
        );

        return user;
    }

    private void validateRequest(
            CreateFamilyTreeRequestDto dto) {

        log.info(
                "<<START>> validateRequest <<START>>"
        );

        if (dto == null
                || dto.getFamilyMembers() == null
                || dto.getFamilyMembers().isEmpty()) {

            throw new RuntimeException(
                    "Family members are required"
            );
        }

        log.info(
                "<<END>> validateRequest <<END>>"
        );
    }

    private void saveFamilyMembers(
            CreateFamilyTreeRequestDto dto,
            UserEntity user) {

        log.info(
                "<<START>> saveFamilyMembers <<START>>"
        );

        List<FamilyMemberEntity> entities =
                dto.getFamilyMembers()
                        .stream()
                        .map(
                                member ->
                                        buildFamilyMember(
                                                member,
                                                user
                                        )
                        )
                        .collect(
                                Collectors.toList()
                        );

        familyMemberRepository
                .saveAll(
                        entities
                );

        log.info(
                "<<END>> saveFamilyMembers <<END>>"
        );
    }

    private FamilyMemberEntity buildFamilyMember(
            CreateFamilyMemberRequestDto dto,
            UserEntity user) {

        FamilyMemberEntity entity =
                familyMemberMapper
                        .toEntity(
                                dto
                        );

        entity.setUser(
                user
        );

        entity.setActive(
                true
        );

        return entity;
    }

    private HomeDashboardResponseDto
    buildDashboardResponse(
            UserEntity user) {

        List<FamilyMemberEntity> members =
                familyMemberRepository
                        .findByUserId(
                                user.getId()
                        );

        return HomeDashboardResponseDto
                .builder()
                .userId(
                        user.getId()
                )
                .fullName(
                        user.getFullName()
                )
                .email(
                        user.getEmail()
                )
                .mobileNumber(
                        user.getMobileNumber()
                )
                .familyMembers(
                        familyMemberMapper
                                .toDtoList(
                                        members
                                )
                )
                .build();
    }

}