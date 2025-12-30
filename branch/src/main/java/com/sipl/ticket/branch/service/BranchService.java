package com.sipl.ticket.branch.service;

import com.sipl.ticket.core.dto.request.BranchSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.BranchDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import org.springframework.stereotype.Service;

@Service
public interface BranchService {

    ApiResponseDTO<BranchDto> saveBranch(BranchDto dto);

    ApiResponseDTO<BranchDto> updateBranch(BranchDto dto);

    ApiResponseDTO<BranchDto> getById(Integer branchId);

    ApiResponseDTO<String> deleteById(Integer branchId);

    ApiResponseDTO<PagedResponse<BranchDto>> searchBranches(
            BranchSearchRequestDto requestDto
    );
}
