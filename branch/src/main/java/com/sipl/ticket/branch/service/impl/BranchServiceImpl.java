package com.sipl.ticket.branch.service.impl;

import com.sipl.ticket.branch.service.BranchService;
import com.sipl.ticket.core.dao.entity.Branches;
import com.sipl.ticket.core.dao.repository.BranchRepository;
import com.sipl.ticket.core.dto.request.BranchSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.BranchDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.mapper.BranchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BranchServiceImpl implements BranchService {

    private final BranchRepository repository;
    private final BranchMapper mapper;

    @Override
    public ApiResponseDTO<BranchDto> saveBranch(BranchDto dto) {

        if (repository.existsByEmailIgnoreCase(dto.getEmail())) {
            return new ApiResponseDTO<>(
                    null,
                    "Branch with email already exists",
                    HttpStatus.CONFLICT,
                    true
            );
        }

        Branches branch = mapper.toEntity(dto);
        branch.setIsActive(true);

        Branches saved = repository.save(branch);

        return new ApiResponseDTO<>(
                mapper.toDto(saved),
                "Branch created successfully",
                HttpStatus.CREATED,
                false
        );
    }

    @Override
    public ApiResponseDTO<BranchDto> updateBranch(BranchDto dto) {

        Branches branch = repository.findById(dto.getBranchId()).orElse(null);

        if (branch == null) {
            return new ApiResponseDTO<>(
                    null,
                    "Branch not found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        if (repository.existsByEmailIgnoreCaseAndBranchIdNot(
                dto.getEmail(), dto.getBranchId())) {

            return new ApiResponseDTO<>(
                    null,
                    "Email already exists",
                    HttpStatus.CONFLICT,
                    true
            );
        }

        Branches updated = mapper.toEntity(dto);
        updated.setIsActive(branch.getIsActive());

        repository.save(updated);

        return new ApiResponseDTO<>(
                mapper.toDto(updated),
                "Branch updated successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    public ApiResponseDTO<BranchDto> getById(Integer branchId) {

        return repository.findById(branchId)
                .filter(b -> Boolean.TRUE.equals(b.getIsActive()))
                .map(b -> new ApiResponseDTO<>(
                        mapper.toDto(b),
                        "Branch found",
                        HttpStatus.OK,
                        false
                ))
                .orElseGet(() -> new ApiResponseDTO<>(
                        null,
                        "Branch not found",
                        HttpStatus.NOT_FOUND,
                        true
                ));
    }

    @Override
    public ApiResponseDTO<String> deleteById(Integer branchId) {

        Branches branch = repository.findById(branchId).orElse(null);

        if (branch == null) {
            return new ApiResponseDTO<>(
                    null,
                    "Branch not found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        branch.setIsActive(false);
        repository.save(branch);

        return new ApiResponseDTO<>(
                null,
                "Branch deleted successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    public ApiResponseDTO<PagedResponse<BranchDto>> searchBranches(
            BranchSearchRequestDto dto) {

        Sort sort = dto.getSortDir().equalsIgnoreCase("asc")
                ? Sort.by(dto.getSortBy()).ascending()
                : Sort.by(dto.getSortBy()).descending();

        Pageable pageable = PageRequest.of(
                dto.getPage(),
                dto.getSize(),
                sort
        );

        Page<Branches> pageResult = repository.searchBranches(
                dto.getBranchId(),
                dto.getCompanyId(),
                dto.getIsActive(),
                pageable
        );

        if (pageResult.isEmpty()) {
            return new ApiResponseDTO<>(
                    null,
                    "No branches found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        List<BranchDto> content = pageResult.getContent()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return new ApiResponseDTO<>(
                new PagedResponse<>(
                        content,
                        pageResult.getNumber(),
                        pageResult.getTotalElements(),
                        pageResult.getTotalPages(),
                        pageResult.getSize(),
                        pageResult.isLast()
                ),
                "Branches fetched successfully",
                HttpStatus.OK,
                false
        );
    }
}
