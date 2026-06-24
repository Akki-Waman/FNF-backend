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
import com.ensf.fnf.family.service.FamilyService;
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
public class FamilyServiceImpl
        implements FamilyService {

    private final UserRepository userRepository;

    private final FamilyMemberRepository familyMemberRepository;

    private final FamilyMemberMapper familyMemberMapper;


}